package org.example.insurance.statement;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policy_statements")
public class PolicyStatement {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long policyId;
    @Column(nullable = false)
    private Long policyholderId;
    @Column(nullable = false)
    private LocalDate statementDate;
    @Column(nullable = false)
    private String statementPeriod;
    private BigDecimal totalPremiumPaid;
    private Integer totalClaims;
    private BigDecimal totalClaimAmount;
    @Column(nullable = false)
    private String generatedBy;
    @Column(nullable = false)
    private String statementStatus = "GENERATED";

    public Long getId() {
        return id;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long v) {
        policyId = v;
    }

    public Long getPolicyholderId() {
        return policyholderId;
    }

    public void setPolicyholderId(Long v) {
        policyholderId = v;
    }

    public LocalDate getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(LocalDate v) {
        statementDate = v;
    }

    public String getStatementPeriod() {
        return statementPeriod;
    }

    public void setStatementPeriod(String v) {
        statementPeriod = v;
    }

    public BigDecimal getTotalPremiumPaid() {
        return totalPremiumPaid;
    }

    public void setTotalPremiumPaid(BigDecimal v) {
        totalPremiumPaid = v;
    }

    public Integer getTotalClaims() {
        return totalClaims;
    }

    public void setTotalClaims(Integer v) {
        totalClaims = v;
    }

    public BigDecimal getTotalClaimAmount() {
        return totalClaimAmount;
    }

    public void setTotalClaimAmount(BigDecimal v) {
        totalClaimAmount = v;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String v) {
        generatedBy = v;
    }

    public String getStatementStatus() {
        return statementStatus;
    }

    public void setStatementStatus(String v) {
        statementStatus = v;
    }
}
