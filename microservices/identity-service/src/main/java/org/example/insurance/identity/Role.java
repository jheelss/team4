package org.example.insurance.identity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "roleName"))
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String roleName;
    private String description;

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
