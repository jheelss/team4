package org.example.insurance.product;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<InsuranceProduct, Long> {
}
