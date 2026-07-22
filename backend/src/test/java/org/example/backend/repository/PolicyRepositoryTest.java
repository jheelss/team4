package org.example.backend.repository;

import org.example.backend.entity.InsuranceProduct;
import org.example.backend.entity.Policy;
import org.example.backend.entity.Policyholder;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
class PolicyRepositoryTest {

    @Autowired
    private InsuranceProductRepository insuranceProductRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private PolicyHolderRepository policyHolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findPolicyByPolicyNumber() {
        InsuranceProduct product = insuranceProductRepository.save(product("Health"));
        Policyholder policyholder = policyHolderRepository.save(policyholder());
        Policy policy = new Policy();
        policy.setPolicyNumber("POL-1001");
        policy.setProductId(product.getProductId());
        policy.setPolicyholderId(policyholder.getPolicyholderId());
        policy.setIssueDate(LocalDate.now());
        policy.setExpiryDate(LocalDate.now().plusYears(1));
        policy.setSumAssured(new BigDecimal("100000.00"));
        policy.setPremiumAmount(new BigDecimal("1200.00"));
        policy.setPolicyStatus("ACTIVE");
        policyRepository.save(policy);

        assertEquals(policy.getPolicyId(), policyRepository.findByPolicyNumber("POL-1001").orElseThrow().getPolicyId());
    }

    private InsuranceProduct product(String type) {
        InsuranceProduct product = new InsuranceProduct();
        product.setProductName(type + " Cover");
        product.setProductType(type);
        product.setCoverageAmount(new BigDecimal("100000.00"));
        product.setPremiumAmount(new BigDecimal("1200.00"));
        product.setPolicyTerm(12);
        product.setStatus("ACTIVE");
        return product;
    }

    private Policyholder policyholder() {
        Role role = new Role();
        role.setRoleName("ROLE_" + System.nanoTime());
        roleRepository.save(role);

        User user = new User();
        user.setUsername("user_" + System.nanoTime());
        user.setPassword("password");
        user.setFullName("Test User");
        user.setEmail("test" + System.nanoTime() + "@example.com");
        user.setRoleId(role.getRoleId());
        user.setStatus("ACTIVE");
        userRepository.save(user);

        Policyholder policyholder = new Policyholder();
        policyholder.setUserId(user.getUserId());
        policyholder.setFirstName("Test");
        policyholder.setLastName("Holder");
        return policyholder;
    }
}
