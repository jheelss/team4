package org.example.backend.repository;

import jakarta.persistence.EntityManager;
import org.example.backend.entity.Role;
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
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesRole() {
        Role role = newRole("ADMIN");

        Role savedRole = roleRepository.save(role);
        entityManager.flush();

        assertNotNull(savedRole.getRoleId());
        assertEquals("ADMIN", savedRole.getRoleName());
        assertEquals("ADMIN role", savedRole.getDescription());
    }

    @Test
    void findsRoleByRoleId() {
        Role savedRole = roleRepository.save(newRole("UNDERWRITER"));
        entityManager.flush();
        entityManager.clear();

        Optional<Role> result = roleRepository.findById(savedRole.getRoleId());

        assertTrue(result.isPresent());
        assertEquals(savedRole.getRoleId(), result.get().getRoleId());
        assertEquals("UNDERWRITER", result.get().getRoleName());
    }

    @Test
    void findsRoleByRoleName() {
        Role savedRole = roleRepository.save(newRole("CLAIMS_OFFICER"));
        entityManager.flush();
        entityManager.clear();

        Optional<Role> result = roleRepository.findByRoleName("CLAIMS_OFFICER");

        assertTrue(result.isPresent());
        assertEquals(savedRole.getRoleId(), result.get().getRoleId());
        assertEquals("CLAIMS_OFFICER", result.get().getRoleName());
    }

    private Role newRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setDescription(roleName + " role");
        return role;
    }
}
