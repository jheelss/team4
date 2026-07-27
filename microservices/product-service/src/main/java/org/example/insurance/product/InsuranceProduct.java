package org.example.insurance.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "insurance_products")
public class InsuranceProduct {
    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private String productType;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal coverageAmount;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;
    @Column(nullable = false)
    private Integer policyTerm;
    private String description;
    @Column(nullable = false)
    private String status = "ACTIVE";

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String v) {
        productName = v;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String v) {
        productType = v;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal v) {
        coverageAmount = v;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal v) {
        premiumAmount = v;
    }

    public Integer getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(Integer v) {
        policyTerm = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        description = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        status = v;
    }
}
