package org.example.insurance.policyholder;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "kyc_documents", uniqueConstraints = @UniqueConstraint(columnNames = "documentNumber"))
public class KycDocument {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long policyholderId;
    @Column(nullable = false)
    private String documentType;
    @Column(nullable = false)
    private String documentNumber;
    private LocalDate uploadDate;
    @Column(nullable = false)
    private String verificationStatus = "PENDING";

    public Long getId() {
        return id;
    }

    public Long getPolicyholderId() {
        return policyholderId;
    }

    public void setPolicyholderId(Long v) {
        policyholderId = v;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String v) {
        documentType = v;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String v) {
        documentNumber = v;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate v) {
        uploadDate = v;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String v) {
        verificationStatus = v;
    }
}
