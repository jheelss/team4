package org.example.backend.repository;

import org.example.backend.entity.PremiumPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PremiumPaymentRepository extends CrudRepository<PremiumPayment, Long> {

    // Find all payments for a policy
    List<PremiumPayment> findByPolicyId(Long policyId);

}
