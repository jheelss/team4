package org.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POLICIES", uniqueConstraints = @UniqueConstraint(name = "UK_POLICIES_POLICY_NUMBER", columnNames = "POLICY_NUMBER"))
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policy_seq_gen")
    @SequenceGenerator(name = "policy_seq_gen", sequenceName = "POLICY_SEQ", allocationSize = 1)
    @Column(name = "POLICY_ID")
    private Long policyId;

    @Column(name = "POLICY_NUMBER", nullable = false, length = 50)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_POLICIES_PRODUCT"))
    private InsuranceProduct product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLICYHOLDER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_POLICIES_POLICYHOLDER"))
    private Policyholder policyholder;

    @Column(name = "ISSUE_DATE", nullable = false)
    private LocalDate issueDate;

    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "RENEWAL_DATE")
    private LocalDate renewalDate;

    @Column(name = "SUM_ASSURED", nullable = false, precision = 15, scale = 2)
    private BigDecimal sumAssured;

    @Column(name = "PREMIUM_AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;

    @Column(name = "POLICY_STATUS", nullable = false, length = 30)
    private String policyStatus;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PremiumPayment> premiumPayments = new ArrayList<>();

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims = new ArrayList<>();

    @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
    private List<PolicyStatement> policyStatements = new ArrayList<>();

    public Policy() {}
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public InsuranceProduct getProduct() { return product; }
    public void setProduct(InsuranceProduct product) { this.product = product; }
    public Policyholder getPolicyholder() { return policyholder; }
    public void setPolicyholder(Policyholder policyholder) { this.policyholder = policyholder; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public LocalDate getRenewalDate() { return renewalDate; }
    public void setRenewalDate(LocalDate renewalDate) { this.renewalDate = renewalDate; }
    public BigDecimal getSumAssured() { return sumAssured; }
    public void setSumAssured(BigDecimal sumAssured) { this.sumAssured = sumAssured; }
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal premiumAmount) { this.premiumAmount = premiumAmount; }
    public String getPolicyStatus() { return policyStatus; }
    public void setPolicyStatus(String policyStatus) { this.policyStatus = policyStatus; }
    public List<PremiumPayment> getPremiumPayments() { return premiumPayments; }
    public void setPremiumPayments(List<PremiumPayment> premiumPayments) { this.premiumPayments = premiumPayments; }
    public List<Claim> getClaims() { return claims; }
    public void setClaims(List<Claim> claims) { this.claims = claims; }
    public List<PolicyStatement> getPolicyStatements() { return policyStatements; }
    public void setPolicyStatements(List<PolicyStatement> policyStatements) { this.policyStatements = policyStatements; }
}
