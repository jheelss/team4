package org.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLES", uniqueConstraints = @UniqueConstraint(name = "UK_ROLES_ROLE_NAME", columnNames = "ROLE_NAME"))
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq_gen")
    @SequenceGenerator(name = "role_seq_gen", sequenceName = "ROLE_SEQ", allocationSize = 1)
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "ROLE_NAME", nullable = false, length = 50)
    private String roleName;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    public Role() {}
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
