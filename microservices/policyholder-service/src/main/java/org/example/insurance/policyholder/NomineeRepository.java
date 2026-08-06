package org.example.insurance.policyholder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface NomineeRepository extends JpaRepository<Nominee, Long> {
    List<Nominee> findByPolicyholderId(Long policyholderId);
}
