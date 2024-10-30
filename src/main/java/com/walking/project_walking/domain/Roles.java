package com.walking.project_walking.domain;

import jakarta.persistence.*;

@Entity

public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false)
    private Long level;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
    private RolesType roles;


    public Roles() {}


    public Roles(Long level, RolesType role) {
        this.level = level;
        this.roles = role;
    }


    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public RolesType getRole() {
        return roles;
    }

    public void setRole(RolesType role) {
        this.roles = roles;
    }


    public enum RolesType {
        ROLE_1,
        ROLE_2,
        ROLE_3
    }
}
