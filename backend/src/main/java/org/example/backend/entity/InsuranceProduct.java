package org.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "INSURANCE_PRODUCTS")
public class InsuranceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
    @SequenceGenerator(name = "product_seq_gen", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_NAME", nullable = false, length = 150)
    private String productName;

    @Column(name = "PRODUCT_TYPE", nullable = false, length = 50)
    private String productType;

    @Column(name = "COVERAGE_AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "PREMIUM_AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;

    @Column(name = "POLICY_TERM", nullable = false)
    private Integer policyTerm;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "STATUS", nullable = false, length = 30)
    private String status;

    public InsuranceProduct() {}
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public BigDecimal getCoverageAmount() { return coverageAmount; }
    public void setCoverageAmount(BigDecimal coverageAmount) { this.coverageAmount = coverageAmount; }
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal premiumAmount) { this.premiumAmount = premiumAmount; }
    public Integer getPolicyTerm() { return policyTerm; }
    public void setPolicyTerm(Integer policyTerm) { this.policyTerm = policyTerm; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
