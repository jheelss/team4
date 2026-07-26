package org.example.insurance.statement;

import org.springframework.data.jpa.repository.JpaRepository;

interface StatementRepository extends JpaRepository<PolicyStatement, Long> {
}
