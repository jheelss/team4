package org.example.backend.repository;

import org.example.backend.entity.Policyholder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyholderRepository extends CrudRepository<Policyholder, Long> {

    Optional<Policyholder> findByUserId(Long userId);
}