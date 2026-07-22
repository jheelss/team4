package org.example.backend.repository;

import org.example.backend.entity.Nominee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NomineeRepository extends CrudRepository<Nominee, Long> {

    Optional<Nominee> findByNomineeId(Long nomineeId);

    List<Nominee> findAllByPolicyholder_PolicyholderId(Long policyholderId);
}
