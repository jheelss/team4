package org.example.insurance.claims;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByPolicyId(Long policyId);
    List<Claim> findByAssessmentStatusOrderByClaimDateAsc(String assessmentStatus);
}
