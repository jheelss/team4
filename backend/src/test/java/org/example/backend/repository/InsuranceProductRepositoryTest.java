package org.example.backend.repository;

import org.example.backend.entity.InsuranceProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
class InsuranceProductRepositoryTest {

    @Autowired
    private InsuranceProductRepository insuranceProductRepository;

    @Test
    void findInsuranceProductById() {
        InsuranceProduct product = insuranceProductRepository.save(product("Life"));

        assertEquals(product.getProductId(), insuranceProductRepository.findByProductId(product.getProductId()).orElseThrow().getProductId());
    }

    private InsuranceProduct product(String type) {
        InsuranceProduct product = new InsuranceProduct();
        product.setProductName(type + " Cover");
        product.setProductType(type);
        product.setCoverageAmount(new java.math.BigDecimal("100000.00"));
        product.setPremiumAmount(new java.math.BigDecimal("1200.00"));
        product.setPolicyTerm(12);
        product.setStatus("ACTIVE");
        return product;
    }
}
