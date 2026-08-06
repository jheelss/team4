package org.example.insurance.policy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policies", uniqueConstraints = @UniqueConstraint(columnNames = "policyNumber"))
public class InsurancePolicy {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String policyNumber;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long policyholderId;
    @Column(nullable = false)
    private LocalDate issueDate;
    @Column(nullable = false)
    private LocalDate expiryDate;
    private LocalDate renewalDate;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal sumAssured;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;
    @Column(nullable = false)
    private String policyStatus = "ACTIVE";
    @JsonIgnore
    @Column(nullable = false)
    private boolean initialPremiumPaid = false;

    public Long getId() {
        return id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String v) {
        policyNumber = v;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long v) {
        productId = v;
    }

    public Long getPolicyholderId() {
        return policyholderId;
    }

    public void setPolicyholderId(Long v) {
        policyholderId = v;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate v) {
        issueDate = v;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate v) {
        expiryDate = v;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate v) {
        renewalDate = v;
    }

    public BigDecimal getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(BigDecimal v) {
        sumAssured = v;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal v) {
        premiumAmount = v;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String v) {
        policyStatus = v;
    }
}
