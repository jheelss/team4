package org.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CLAIMS")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_seq_gen")
    @SequenceGenerator(name = "claim_seq_gen", sequenceName = "CLAIM_SEQ", allocationSize = 1)
    @Column(name = "CLAIM_ID")
    private Long claimId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLICY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CLAIMS_POLICY"))
    private Policy policy;

    @Column(name = "CLAIM_DATE", nullable = false)
    private LocalDate claimDate;

    @Column(name = "CLAIM_AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal claimAmount;

    @Column(name = "CLAIM_REASON", nullable = false, length = 500)
    private String claimReason;

    @Column(name = "ASSESSMENT_STATUS", length = 30)
    private String assessmentStatus;

    @Column(name = "SETTLEMENT_AMOUNT", precision = 15, scale = 2)
    private BigDecimal settlementAmount;

    @Column(name = "SETTLEMENT_DATE")
    private LocalDate settlementDate;

    @Column(name = "CLAIM_STATUS", nullable = false, length = 30)
    private String claimStatus;

    public Claim() {}
    public Long getClaimId() { return claimId; }
    public void setClaimId(Long claimId) { this.claimId = claimId; }
    public Policy getPolicy() { return policy; }
    public void setPolicy(Policy policy) { this.policy = policy; }
    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }
    public BigDecimal getClaimAmount() { return claimAmount; }
    public void setClaimAmount(BigDecimal claimAmount) { this.claimAmount = claimAmount; }
    public String getClaimReason() { return claimReason; }
    public void setClaimReason(String claimReason) { this.claimReason = claimReason; }
    public String getAssessmentStatus() { return assessmentStatus; }
    public void setAssessmentStatus(String assessmentStatus) { this.assessmentStatus = assessmentStatus; }
    public BigDecimal getSettlementAmount() { return settlementAmount; }
    public void setSettlementAmount(BigDecimal settlementAmount) { this.settlementAmount = settlementAmount; }
    public LocalDate getSettlementDate() { return settlementDate; }
    public void setSettlementDate(LocalDate settlementDate) { this.settlementDate = settlementDate; }
    public String getClaimStatus() { return claimStatus; }
    public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
}
