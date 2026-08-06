package org.example.insurance.premium;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "premium_payments")
public class PremiumPayment {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long policyId;
    @Column(nullable = false)
    private LocalDate paymentDate;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private String paymentMethod;
    @Column(nullable = false)
    private String paymentStatus = "SUCCESS";

    public Long getId() {
        return id;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long v) {
        policyId = v;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate v) {
        paymentDate = v;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal v) {
        amount = v;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String v) {
        paymentMethod = v;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String v) {
        paymentStatus = v;
    }
}
