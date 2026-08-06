package org.example.insurance.policy;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface PolicyRepository extends JpaRepository<InsurancePolicy, Long> {
    List<InsurancePolicy> findByPolicyholderIdOrderByIssueDateDesc(Long policyholderId);
    List<InsurancePolicy> findByPolicyStatusOrderByIssueDateAsc(String policyStatus);
}
