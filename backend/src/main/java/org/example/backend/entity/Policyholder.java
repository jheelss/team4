package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POLICYHOLDERS", uniqueConstraints = @UniqueConstraint(name = "UK_POLICYHOLDERS_USER", columnNames = "USER_ID"))
public class Policyholder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policyholder_seq_gen")
    @SequenceGenerator(name = "policyholder_seq_gen", sequenceName = "POLICYHOLDER_SEQ", allocationSize = 1)
    @Column(name = "POLICYHOLDER_ID")
    private Long policyholderId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true, foreignKey = @ForeignKey(name = "FK_POLICYHOLDERS_USER"))
    private User user;

    @Column(name = "FIRST_NAME", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 100)
    private String lastName;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "GENDER", length = 20)
    private String gender;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "ADDRESS", length = 500)
    private String address;

    @Column(name = "KYC_STATUS", length = 30)
    private String kycStatus;

    @OneToMany(mappedBy = "policyholder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nominee> nominees = new ArrayList<>();

    @OneToMany(mappedBy = "policyholder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KycDocument> kycDocuments = new ArrayList<>();

    @OneToMany(mappedBy = "policyholder", fetch = FetchType.LAZY)
    private List<Policy> policies = new ArrayList<>();

    @OneToMany(mappedBy = "policyholder", fetch = FetchType.LAZY)
    private List<PolicyStatement> policyStatements = new ArrayList<>();

    public Policyholder() {}
    public Long getPolicyholderId() { return policyholderId; }
    public void setPolicyholderId(Long policyholderId) { this.policyholderId = policyholderId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getKycStatus() { return kycStatus; }
    public void setKycStatus(String kycStatus) { this.kycStatus = kycStatus; }
    public List<Nominee> getNominees() { return nominees; }
    public void setNominees(List<Nominee> nominees) { this.nominees = nominees; }
    public List<KycDocument> getKycDocuments() { return kycDocuments; }
    public void setKycDocuments(List<KycDocument> kycDocuments) { this.kycDocuments = kycDocuments; }
    public List<Policy> getPolicies() { return policies; }
    public void setPolicies(List<Policy> policies) { this.policies = policies; }
    public List<PolicyStatement> getPolicyStatements() { return policyStatements; }
    public void setPolicyStatements(List<PolicyStatement> policyStatements) { this.policyStatements = policyStatements; }
}
