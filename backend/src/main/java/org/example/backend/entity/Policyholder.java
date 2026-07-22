package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "POLICYHOLDERS", uniqueConstraints = @UniqueConstraint(name = "UK_POLICYHOLDERS_USER", columnNames = "USER_ID"))
public class Policyholder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policyholder_seq_gen")
    @SequenceGenerator(name = "policyholder_seq_gen", sequenceName = "POLICYHOLDER_SEQ", allocationSize = 1)
    @Column(name = "POLICYHOLDER_ID")
    private Long policyholderId;

    @Column(name = "USER_ID", nullable = false, unique = true)
    private Long userId;

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

    public Policyholder() {}
    public Long getPolicyholderId() { return policyholderId; }
    public void setPolicyholderId(Long policyholderId) { this.policyholderId = policyholderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
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
}
