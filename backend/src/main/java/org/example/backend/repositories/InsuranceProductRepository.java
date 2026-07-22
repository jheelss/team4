package org.example.backend.repositories;

import org.example.backend.entity.InsuranceProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceProductRepository extends CrudRepository<InsuranceProduct, Long> {

    List<InsuranceProduct> findAllByProductType(String productType);
}
