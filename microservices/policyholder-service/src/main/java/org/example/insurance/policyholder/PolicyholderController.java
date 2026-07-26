package org.example.insurance.policyholder;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/policyholders")
public class PolicyholderController {
    private final PolicyholderRepository holders;
    private final NomineeRepository nominees;
    private final KycDocumentRepository documents;
    private final RestClient identity;

    PolicyholderController(PolicyholderRepository holders, NomineeRepository nominees, KycDocumentRepository documents,
            @Value("${clients.identity-url}") String identityUrl) {
        this.holders = holders; this.nominees = nominees; this.documents = documents;
        this.identity = RestClient.create(identityUrl);
    }

    @PostMapping
    public ResponseEntity<Policyholder> create(@RequestBody Policyholder holder) {
        Map<?, ?> user;
        try { user = identity.get().uri("/users/{id}", holder.getUserId()).retrieve().body(Map.class); }
        catch (Exception e) { return ResponseEntity.unprocessableEntity().build(); }
        if (!"ACTIVE".equals(user.get("status")) || !"POLICYHOLDER".equals(user.get("role")))
            return ResponseEntity.unprocessableEntity().build();
        return ResponseEntity.status(201).body(holders.save(holder));
    }

    @GetMapping("/{id}") public Policyholder get(@PathVariable Long id) { return holders.findById(id).orElseThrow(() -> new NoSuchElementException("Policyholder not found")); }
    @PostMapping("/{id}/nominees") public ResponseEntity<Nominee> addNominee(@PathVariable Long id, @RequestBody Nominee nominee) { get(id); nominee.setPolicyholderId(id); return ResponseEntity.status(201).body(nominees.save(nominee)); }
    @GetMapping("/{id}/nominees") public List<Nominee> nomineeList(@PathVariable Long id) { return nominees.findByPolicyholderId(id); }

    @PostMapping("/{id}/kyc-documents")
    public ResponseEntity<KycDocument> addDocument(@PathVariable Long id, @RequestBody KycDocument document) {
        get(id); document.setPolicyholderId(id); KycDocument saved = documents.save(document); refreshKycStatus(id); return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{holderId}/kyc-documents/{documentId}/verification-status")
    public KycDocument updateDocumentStatus(@PathVariable Long holderId, @PathVariable Long documentId, @RequestParam String value) {
        KycDocument document = documents.findById(documentId).filter(d -> d.getPolicyholderId().equals(holderId)).orElseThrow(() -> new NoSuchElementException("KYC document not found"));
        document.setVerificationStatus(value); KycDocument saved = documents.save(document); refreshKycStatus(holderId); return saved;
    }

    @GetMapping("/{id}/eligibility")
    public Map<String, Object> eligible(@PathVariable Long id) { Policyholder h = get(id); return Map.of("policyholderId", id, "eligible", "VERIFIED".equals(h.getKycStatus()), "kycStatus", h.getKycStatus()); }

    private void refreshKycStatus(Long holderId) {
        Policyholder holder = get(holderId);
        boolean verified = documents.findByPolicyholderId(holderId).stream().anyMatch(d -> "VERIFIED".equals(d.getVerificationStatus()));
        holder.setKycStatus(verified ? "VERIFIED" : "PENDING"); holders.save(holder);
    }
}
