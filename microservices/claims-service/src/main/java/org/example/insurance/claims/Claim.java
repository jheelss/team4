package org.example.insurance.claims;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "claims")
public class Claim {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long policyId;
    @Column(nullable = false)
    private LocalDate claimDate;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal claimAmount;
    @Column(nullable = false)
    private String claimReason;
    private String assessmentStatus = "PENDING";
    private BigDecimal settlementAmount;
    private LocalDate settlementDate;
    @Column(nullable = false)
    private String claimStatus = "REGISTERED";

    public Long getId() {
        return id;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long v) {
        policyId = v;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate v) {
        claimDate = v;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal v) {
        claimAmount = v;
    }

    public String getClaimReason() {
        return claimReason;
    }

    public void setClaimReason(String v) {
        claimReason = v;
    }

    public String getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(String v) {
        assessmentStatus = v;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(BigDecimal v) {
        settlementAmount = v;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate v) {
        settlementDate = v;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String v) {
        claimStatus = v;
    }
}
