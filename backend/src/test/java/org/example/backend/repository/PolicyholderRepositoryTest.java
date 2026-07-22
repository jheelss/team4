package org.example.backend.repository;

import jakarta.persistence.EntityManager;
import org.example.backend.entity.Policyholder;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class PolicyholderRepositoryTest {

    @Autowired
    private PolicyholderRepository policyholderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesPolicyholder() {
        User user = saveUser("holder.save", "holder.save@example.com");
        Policyholder policyholder = newPolicyholder(user);

        Policyholder savedPolicyholder = policyholderRepository.save(policyholder);
        entityManager.flush();

        assertNotNull(savedPolicyholder.getPolicyholderId());
        assertEquals("Asha", savedPolicyholder.getFirstName());
        assertEquals("Fernandes", savedPolicyholder.getLastName());
    }

    @Test
    void findsPolicyholderByPolicyholderId() {
        User user = saveUser("holder.id", "holder.id@example.com");
        Policyholder savedPolicyholder = policyholderRepository.save(newPolicyholder(user));
        entityManager.flush();
        entityManager.clear();

        Optional<Policyholder> result = policyholderRepository.findById(
                savedPolicyholder.getPolicyholderId()
        );

        assertTrue(result.isPresent());
        assertEquals(savedPolicyholder.getPolicyholderId(), result.get().getPolicyholderId());
    }

    @Test
    void findsPolicyholderByUserId() {
        User user = saveUser("holder.user", "holder.user@example.com");
        Policyholder savedPolicyholder = policyholderRepository.save(newPolicyholder(user));
        entityManager.flush();
        entityManager.clear();

        Optional<Policyholder> result = policyholderRepository.findByUser_UserId(user.getUserId());

        assertTrue(result.isPresent());
        assertEquals(savedPolicyholder.getPolicyholderId(), result.get().getPolicyholderId());
    }

    @Test
    void persistsUserLink() {
        User user = saveUser("holder.link", "holder.link@example.com");
        Policyholder savedPolicyholder = policyholderRepository.save(newPolicyholder(user));
        entityManager.flush();
        entityManager.clear();

        Policyholder result = policyholderRepository.findById(
                savedPolicyholder.getPolicyholderId()
        ).orElseThrow();

        assertNotNull(result.getUser());
        assertEquals(user.getUserId(), result.getUser().getUserId());
        assertEquals("holder.link", result.getUser().getUsername());
    }

    private User saveUser(String username, String email) {
        Role role = new Role();
        role.setRoleName("POLICYHOLDER_" + username);
        role.setDescription("Policyholder role");
        role = roleRepository.save(role);

        User user = new User();
        user.setUsername(username);
        user.setPassword("hashed-test-password");
        user.setFullName("Asha Fernandes");
        user.setEmail(email);
        user.setPhone("9999999999");
        user.setRole(role);
        user.setStatus("ACTIVE");
        return userRepository.save(user);
    }

    private Policyholder newPolicyholder(User user) {
        Policyholder policyholder = new Policyholder();
        policyholder.setUser(user);
        policyholder.setFirstName("Asha");
        policyholder.setLastName("Fernandes");
        policyholder.setDob(LocalDate.of(1990, 1, 15));
        policyholder.setGender("FEMALE");
        policyholder.setEmail(user.getEmail());
        policyholder.setPhone(user.getPhone());
        policyholder.setAddress("Test Address");
        policyholder.setKycStatus("VERIFIED");
        return policyholder;
    }
}
