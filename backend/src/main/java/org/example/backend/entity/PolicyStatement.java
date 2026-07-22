package org.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "POLICY_STATEMENTS")
public class PolicyStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statement_seq_gen")
    @SequenceGenerator(name = "statement_seq_gen", sequenceName = "STATEMENT_SEQ", allocationSize = 1)
    @Column(name = "STATEMENT_ID")
    private Long statementId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLICYHOLDER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_STATEMENTS_POLICYHOLDER"))
    private Policyholder policyholder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLICY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_STATEMENTS_POLICY"))
    private Policy policy;

    @Column(name = "STATEMENT_DATE", nullable = false)
    private LocalDate statementDate;

    @Column(name = "STATEMENT_PERIOD", nullable = false, length = 50)
    private String statementPeriod;

    @Column(name = "TOTAL_PREMIUM_PAID", precision = 15, scale = 2)
    private BigDecimal totalPremiumPaid;

    @Column(name = "TOTAL_CLAIMS")
    private Integer totalClaims;

    @Column(name = "TOTAL_CLAIM_AMOUNT", precision = 15, scale = 2)
    private BigDecimal totalClaimAmount;

    @Column(name = "GENERATED_BY", nullable = false, length = 100)
    private String generatedBy;

    @Column(name = "STATEMENT_STATUS", nullable = false, length = 30)
    private String statementStatus;

    public PolicyStatement() {}
    public Long getStatementId() { return statementId; }
    public void setStatementId(Long statementId) { this.statementId = statementId; }
    public Policyholder getPolicyholder() { return policyholder; }
    public void setPolicyholder(Policyholder policyholder) { this.policyholder = policyholder; }
    public Policy getPolicy() { return policy; }
    public void setPolicy(Policy policy) { this.policy = policy; }
    public LocalDate getStatementDate() { return statementDate; }
    public void setStatementDate(LocalDate statementDate) { this.statementDate = statementDate; }
    public String getStatementPeriod() { return statementPeriod; }
    public void setStatementPeriod(String statementPeriod) { this.statementPeriod = statementPeriod; }
    public BigDecimal getTotalPremiumPaid() { return totalPremiumPaid; }
    public void setTotalPremiumPaid(BigDecimal totalPremiumPaid) { this.totalPremiumPaid = totalPremiumPaid; }
    public Integer getTotalClaims() { return totalClaims; }
    public void setTotalClaims(Integer totalClaims) { this.totalClaims = totalClaims; }
    public BigDecimal getTotalClaimAmount() { return totalClaimAmount; }
    public void setTotalClaimAmount(BigDecimal totalClaimAmount) { this.totalClaimAmount = totalClaimAmount; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public String getStatementStatus() { return statementStatus; }
    public void setStatementStatus(String statementStatus) { this.statementStatus = statementStatus; }
}
