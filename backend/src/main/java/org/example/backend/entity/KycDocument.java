package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "KYC_DOCUMENTS")
public class KycDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kyc_document_seq_gen")
    @SequenceGenerator(name = "kyc_document_seq_gen", sequenceName = "KYC_DOCUMENT_SEQ", allocationSize = 1)
    @Column(name = "DOCUMENT_ID")
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLICYHOLDER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_KYC_POLICYHOLDER"))
    private Policyholder policyholder;

    @Column(name = "DOCUMENT_TYPE", nullable = false, length = 50)
    private String documentType;

    @Column(name = "DOCUMENT_NUMBER", nullable = false, length = 100)
    private String documentNumber;

    @Column(name = "UPLOAD_DATE", nullable = false)
    private LocalDate uploadDate;

    @Column(name = "VERIFICATION_STATUS", nullable = false, length = 30)
    private String verificationStatus;

    public KycDocument() {}
    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }
    public Policyholder getPolicyholder() { return policyholder; }
    public void setPolicyholder(Policyholder policyholder) { this.policyholder = policyholder; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public LocalDate getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDate uploadDate) { this.uploadDate = uploadDate; }
    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
}
