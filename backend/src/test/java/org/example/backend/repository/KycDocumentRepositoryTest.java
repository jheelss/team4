package org.example.backend.repository;

import org.example.backend.entity.KycDocument;
import org.example.backend.entity.Policyholder;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
class KycDocumentRepositoryTest {

    @Autowired
    private KycDocumentRepository kycDocumentRepository;

    @Autowired
    private PolicyHolderRepository policyHolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findKycDocumentById() {
        Policyholder policyholder = policyHolderRepository.save(policyholder());
        KycDocument document = new KycDocument();
        document.setPolicyholderId(policyholder.getPolicyholderId());
        document.setDocumentType("PAN");
        document.setDocumentNumber("ABCDE1234F");
        document.setUploadDate(LocalDate.now());
        document.setVerificationStatus("VERIFIED");
        kycDocumentRepository.save(document);

        assertEquals(document.getDocumentId(), kycDocumentRepository.findByDocumentId(document.getDocumentId()).orElseThrow().getDocumentId());
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
