package org.example.backend.repository;

import jakarta.persistence.EntityManager;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesUser() {
        Role role = saveRole("ADMIN");
        User user = newUser("admin.user", "admin.user@example.com", role);

        User savedUser = userRepository.save(user);
        entityManager.flush();

        assertNotNull(savedUser.getUserId());
        assertNotNull(savedUser.getCreatedAt());
        assertEquals("admin.user", savedUser.getUsername());
    }

    @Test
    void findsUserByUserId() {
        Role role = saveRole("UNDERWRITER");
        User savedUser = userRepository.save(
                newUser("underwriter.user", "underwriter.user@example.com", role)
        );
        entityManager.flush();
        entityManager.clear();

        Optional<User> result = userRepository.findById(savedUser.getUserId());

        assertTrue(result.isPresent());
        assertEquals(savedUser.getUserId(), result.get().getUserId());
        assertEquals("underwriter.user", result.get().getUsername());
    }

    @Test
    void findsUserByUsername() {
        Role role = saveRole("CLAIMS_OFFICER");
        User savedUser = userRepository.save(
                newUser("claims.user", "claims.user@example.com", role)
        );
        entityManager.flush();
        entityManager.clear();

        Optional<User> result = userRepository.findByUsername("claims.user");

        assertTrue(result.isPresent());
        assertEquals(savedUser.getUserId(), result.get().getUserId());
    }

    @Test
    void findsUserByEmail() {
        Role role = saveRole("POLICYHOLDER");
        User savedUser = userRepository.save(
                newUser("policy.user", "policy.user@example.com", role)
        );
        entityManager.flush();
        entityManager.clear();

        Optional<User> result = userRepository.findByEmail("policy.user@example.com");

        assertTrue(result.isPresent());
        assertEquals(savedUser.getUserId(), result.get().getUserId());
    }

    @Test
    void persistsRoleIdLink() {
        Role role = saveRole("ADMIN");
        User savedUser = userRepository.save(
                newUser("linked.user", "linked.user@example.com", role)
        );
        entityManager.flush();
        entityManager.clear();

        User result = userRepository.findById(savedUser.getUserId()).orElseThrow();

        assertNotNull(result.getRole());
        assertEquals(role.getRoleId(), result.getRole().getRoleId());
        assertEquals("ADMIN", result.getRole().getRoleName());
    }

    private Role saveRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setDescription(roleName + " role");
        return roleRepository.save(role);
    }

    private User newUser(String username, String email, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("hashed-test-password");
        user.setFullName("Repository Test User");
        user.setEmail(email);
        user.setPhone("9999999999");
        user.setRole(role);
        user.setStatus("ACTIVE");
        return user;
    }
}
