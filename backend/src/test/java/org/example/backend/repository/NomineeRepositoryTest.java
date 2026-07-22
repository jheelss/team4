package org.example.backend.repository;

import org.example.backend.entity.Nominee;
import org.example.backend.entity.Policyholder;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
class NomineeRepositoryTest {

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private PolicyHolderRepository policyHolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findNomineeByPolicyHolder() {
        Policyholder policyholder = policyHolderRepository.save(policyholder());
        Nominee nominee = new Nominee();
        nominee.setPolicyholderId(policyholder.getPolicyholderId());
        nominee.setNomineeName("Alex Doe");
        nominee.setRelationship("Child");
        nomineeRepository.save(nominee);

        assertEquals(List.of(nominee.getNomineeId()), nomineeRepository.findAllByPolicyholderId(policyholder.getPolicyholderId()).stream().map(Nominee::getNomineeId).toList());
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
