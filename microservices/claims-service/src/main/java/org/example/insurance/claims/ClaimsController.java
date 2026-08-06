package org.example.insurance.claims;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/claims")
public class ClaimsController {
    private final ClaimRepository claims;
    private final RestClient policies;

    ClaimsController(
            ClaimRepository claims,
            @LoadBalanced RestClient.Builder restClientBuilder,
            @Value("${clients.policy-url}") String url
    ) {
        this.claims = claims;
        policies = restClientBuilder.clone().baseUrl(url).build();
    }

    @PostMapping
    public ResponseEntity<Claim> register(@RequestBody Claim claim) {
        Map<?, ?> policy = policies.get().uri("/policies/{id}", claim.getPolicyId()).retrieve().body(Map.class);
        if (!"ACTIVE".equals(policy.get("policyStatus")))
            return ResponseEntity.unprocessableEntity().build();
        return ResponseEntity.status(201).body(claims.save(claim));
    }

    @GetMapping("/policy/{id}")
    public List<Claim> byPolicy(@PathVariable Long id) {
        return claims.findByPolicyId(id);
    }

    @GetMapping("/pending")
    public List<Claim> pending() {
        return claims.findByAssessmentStatusOrderByClaimDateAsc("PENDING");
    }

    @PutMapping("/{id}/settlement")
    public Claim settle(@PathVariable Long id, @RequestParam String status,
            @RequestParam(required = false) java.math.BigDecimal amount) {
        Claim c = claims.findById(id).orElseThrow(() -> new NoSuchElementException("Claim not found"));
        c.setAssessmentStatus("ASSESSED");
        c.setClaimStatus(status);
        c.setSettlementAmount(amount);
        c.setSettlementDate(LocalDate.now());
        return claims.save(c);
    }
}
