package com.walking.project_walking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false)
    private Long level;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
    private RolesType roles;

    public enum RolesType {
        ROLE_1,
        ROLE_2,
        ROLE_3
    }
}