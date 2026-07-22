package org.example.backend.repositories;

import org.example.backend.entity.PolicyStatement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyStatementRepository extends CrudRepository<PolicyStatement, Long> {

    // Find all statements for a policyholder
    List<PolicyStatement> findByPolicyholder_PolicyholderId(Long policyholderId);

    // Find all statements for a policy
    List<PolicyStatement> findByPolicy_PolicyId(Long policyId);

}
