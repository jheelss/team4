package org.example.insurance.policyholder;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "policyholders")
public class Policyholder {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private Long userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private LocalDate dob;
    private String email;
    private String phone;
    private String address;
    @Column(nullable = false)
    private String kycStatus = "PENDING";

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long v) {
        userId = v;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String v) {
        firstName = v;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String v) {
        lastName = v;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate v) {
        dob = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        email = v;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String v) {
        phone = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String v) {
        address = v;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String v) {
        kycStatus = v;
    }
}
