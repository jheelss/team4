package org.example.backend.repository;

import org.example.backend.entity.InsuranceProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceProductRepository extends CrudRepository<InsuranceProduct, Long> {

    Optional<InsuranceProduct> findByProductId(Long productId);

    List<InsuranceProduct> findAllByProductType(String productType);

}