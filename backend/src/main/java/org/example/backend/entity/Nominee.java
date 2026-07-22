package org.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "NOMINEES")
public class Nominee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nominee_seq_gen")
    @SequenceGenerator(name = "nominee_seq_gen", sequenceName = "NOMINEE_SEQ", allocationSize = 1)
    @Column(name = "NOMINEE_ID")
    private Long nomineeId;

    @Column(name = "POLICYHOLDER_ID", nullable = false)
    private Long policyholderId;

    @Column(name = "NOMINEE_NAME", nullable = false, length = 150)
    private String nomineeName;

    @Column(name = "RELATIONSHIP", nullable = false, length = 50)
    private String relationship;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "CONTACT_NO", length = 20)
    private String contactNo;

    public Nominee() {}
    public Long getNomineeId() { return nomineeId; }
    public void setNomineeId(Long nomineeId) { this.nomineeId = nomineeId; }
    public Long getPolicyholderId() { return policyholderId; }
    public void setPolicyholderId(Long policyholderId) { this.policyholderId = policyholderId; }
    public String getNomineeName() { return nomineeName; }
    public void setNomineeName(String nomineeName) { this.nomineeName = nomineeName; }
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }
}
