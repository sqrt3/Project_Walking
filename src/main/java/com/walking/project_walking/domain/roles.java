package com.walking.project_walking.domain;
import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false)
    private Long level;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role;


    public Role() {}


    public Role(Long level, RoleType role) {
        this.level = level;
        this.role = role;
    }


    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }


    public enum RoleType {
        ROLE_1,
        ROLE_2,
        ROLE_3
    }
}
