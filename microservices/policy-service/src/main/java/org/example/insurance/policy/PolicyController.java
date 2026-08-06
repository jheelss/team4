package org.example.insurance.policy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyRepository policies;
    private final RestClient holders;
    private final RestClient products;

    PolicyController(
            PolicyRepository policies,
            @LoadBalanced RestClient.Builder restClientBuilder,
            @Value("${clients.policyholder-url}") String policyholderUrl,
            @Value("${clients.product-url}") String productUrl
    ) {
        this.policies = policies;
        this.holders = restClientBuilder.clone().baseUrl(policyholderUrl).build();
        this.products = restClientBuilder.clone().baseUrl(productUrl).build();
    }

    record IssueRequest(
            Long policyholderId,
            Long productId,
            LocalDate issueDate,
            LocalDate expiryDate,
            BigDecimal sumAssured
    ) {
    }

    @PostMapping
    public ResponseEntity<InsurancePolicy> issue(@RequestBody IssueRequest request) {
        Map<?, ?> eligibility = holders.get()
                .uri("/policyholders/{id}/eligibility", request.policyholderId())
                .retrieve()
                .body(Map.class);
        if (!Boolean.TRUE.equals(eligibility.get("eligible"))) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Map<?, ?> product = products.get()
                .uri("/products/{id}", request.productId())
                .retrieve()
                .body(Map.class);
        if (!"ACTIVE".equals(product.get("status"))) {
            return ResponseEntity.unprocessableEntity().build();
        }
        InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyholderId(request.policyholderId());
        policy.setProductId(request.productId());
        policy.setIssueDate(request.issueDate());
        policy.setExpiryDate(request.expiryDate());
        policy.setSumAssured(request.sumAssured());
        policy.setPremiumAmount(new BigDecimal(product.get("premiumAmount").toString()));
        policy.setPolicyNumber("POL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        policy.setPolicyStatus("PENDING_APPROVAL");
        return ResponseEntity.status(201).body(policies.save(policy));
    }

    @GetMapping("/{id}")
    public InsurancePolicy get(@PathVariable Long id) {
        return policies.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Policy not found"));
    }

    @GetMapping("/policyholder/{policyholderId}")
    public List<InsurancePolicy> byPolicyholder(@PathVariable Long policyholderId) {
        return policies.findByPolicyholderIdOrderByIssueDateDesc(policyholderId);
    }

    @GetMapping("/pending")
    public List<InsurancePolicy> pending() {
        return policies.findByPolicyStatusOrderByIssueDateAsc("PENDING_APPROVAL");
    }

    @PutMapping("/{id}/approval")
    public ResponseEntity<InsurancePolicy> approve(@PathVariable Long id, @RequestParam String status) {
        if (!java.util.Set.of("ACTIVE", "REJECTED").contains(status)) {
            return ResponseEntity.badRequest().build();
        }
        InsurancePolicy policy = get(id);
        if (!"PENDING_APPROVAL".equals(policy.getPolicyStatus())) {
            return ResponseEntity.unprocessableEntity().build();
        }
        policy.setPolicyStatus(status);
        return ResponseEntity.ok(policies.save(policy));
    }

    @PutMapping("/{id}/renew")
    public InsurancePolicy renew(
            @PathVariable Long id,
            @RequestParam LocalDate renewalDate,
            @RequestParam LocalDate expiryDate
    ) {
        InsurancePolicy policy = get(id);
        policy.setRenewalDate(renewalDate);
        policy.setExpiryDate(expiryDate);
        policy.setPolicyStatus("ACTIVE");
        return policies.save(policy);
    }
}
