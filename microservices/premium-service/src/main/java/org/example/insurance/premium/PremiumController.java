package org.example.insurance.premium;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/payments")
public class PremiumController {

    private final PaymentRepository payments;
    private final RestClient policies;

    PremiumController(
            PaymentRepository payments,
            @LoadBalanced RestClient.Builder restClientBuilder,
            @Value("${clients.policy-url}") String policyUrl
    ) {
        this.payments = payments;
        this.policies = restClientBuilder.clone().baseUrl(policyUrl).build();
    }

    @PostMapping
    public ResponseEntity<PremiumPayment> record(@RequestBody PremiumPayment payment) {
        Map<?, ?> policy = policies.get()
                .uri("/policies/{id}", payment.getPolicyId())
                .retrieve()
                .body(Map.class);
        if (!"ACTIVE".equals(policy.get("policyStatus"))) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.status(201).body(payments.save(payment));
    }

    @GetMapping("/policy/{id}")
    public List<PremiumPayment> byPolicy(@PathVariable Long id) {
        return payments.findByPolicyId(id);
    }

    @GetMapping("/policy/{id}/summary")
    public Map<String, Object> summary(@PathVariable Long id) {
        BigDecimal total = payments.findByPolicyId(id).stream()
                .filter(payment -> "SUCCESS".equals(payment.getPaymentStatus()))
                .map(PremiumPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Map.of("policyId", id, "totalPremiumPaid", total);
    }
}
