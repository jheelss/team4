package org.example.insurance.policyholder;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface PolicyholderRepository extends JpaRepository<Policyholder, Long> {
    Optional<Policyholder> findByUserId(Long userId);
}
