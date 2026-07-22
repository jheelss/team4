package org.example.backend.repository;

import org.example.backend.entity.Policy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends CrudRepository<Policy, Long> {

    // Find by policy number
    Optional<Policy> findByPolicyNumber(String policyNumber);

    // Find all policies under a product
    List<Policy> findByProductId(Long productId);

    // Find all policies owned by a policyholder
    List<Policy> findByPolicyholderId(Long policyholderId);

}
