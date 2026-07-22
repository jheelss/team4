package org.example.backend.repository;

import org.example.backend.entity.Claim;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends CrudRepository<Claim, Long> {

    // Find all claims for a specific policy
    List<Claim> findByPolicyId(Long policyId);

    // Optional: Find claims by status
    List<Claim> findByClaimStatus(String claimStatus);

}
