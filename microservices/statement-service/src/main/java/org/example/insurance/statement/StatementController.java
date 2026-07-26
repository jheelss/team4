package org.example.insurance.statement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/statements")
public class StatementController {
    private final StatementRepository statements;
    private final RestClient policies, premiums, claims;

    StatementController(StatementRepository s, @Value("${clients.policy-url}") String p,
            @Value("${clients.premium-url}") String pr, @Value("${clients.claims-url}") String c) {
        statements = s;
        policies = RestClient.create(p);
        premiums = RestClient.create(pr);
        claims = RestClient.create(c);
    }

    @PostMapping("/policy/{id}")
    public ResponseEntity<PolicyStatement> generate(@PathVariable Long id,
            @RequestParam(defaultValue = "SYSTEM") String generatedBy) {
        Map<?, ?> policy = policies.get().uri("/policies/{id}", id).retrieve().body(Map.class);
        Map<?, ?> payment = premiums.get().uri("/payments/policy/{id}/summary", id).retrieve().body(Map.class);
        List<?> claimList = claims.get().uri("/claims/policy/{id}", id).retrieve().body(List.class);
        BigDecimal claimTotal = claimList.stream().map(x -> (Map<?, ?>) x)
                .map(x -> new BigDecimal(x.get("claimAmount").toString())).reduce(BigDecimal.ZERO, BigDecimal::add);
        PolicyStatement s = new PolicyStatement();
        s.setPolicyId(id);
        s.setPolicyholderId(Long.valueOf(policy.get("policyholderId").toString()));
        s.setStatementDate(LocalDate.now());
        s.setStatementPeriod("CURRENT");
        s.setTotalPremiumPaid(new BigDecimal(payment.get("totalPremiumPaid").toString()));
        s.setTotalClaims(claimList.size());
        s.setTotalClaimAmount(claimTotal);
        s.setGeneratedBy(generatedBy);
        return ResponseEntity.status(201).body(statements.save(s));
    }

    @GetMapping("/{id}")
    public PolicyStatement get(@PathVariable Long id) {
        return statements.findById(id).orElseThrow(() -> new NoSuchElementException("Statement not found"));
    }
}
