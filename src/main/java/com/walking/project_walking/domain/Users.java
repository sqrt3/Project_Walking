package com.walking.project_walking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "phone", nullable = false, length = 16)
    private String phone;

    @Column(name = "nickname", nullable = false, length = 256)
    private String nickname;

    @Column(name = "gender", nullable = false) // TINYINT를 해주려면 Boolean으로 해줘야 함.
    private Boolean gender;

    @Column(name = "birth", nullable = false)
    @Temporal(TemporalType.DATE) // Date 타입 포맷을 맞춰준다고 함. (yyyy-MM-dd 형태)
    private LocalDate birth;

    @CreatedDate // 계정 생성 날짜
    @Column(name = "join_Date", nullable = false)
    private LocalDate joinDate; // date 형식이기 때문에 LocalDate(yyyy-MM-dd)로 변경

    @LastModifiedDate // 마지막 접속한 날짜, 로그아웃 시 업데이트 되도록 설정할 것.
    @Column(name = "last_login")
    private LocalDateTime lastLogin; // TIMESTAMP 형식이기 때문에 LocalDateTime으로 둠.

    @Column(name = "login_count", nullable = false)
    private Long loginCount;

    @Column(name = "login_browser", nullable = false, length = 512)
    private String loginBrowser;

    @Column(name = "user_level")
    private Integer userLevel;

    @Column(name = "user_exp")
    private Long userExp;

    @Column(name = "point")
    private Integer point;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "profile_image", length = 256) // url 형태로 받아와도 되는 형태 (VARCHAR 이므로)
    private String profileImage;
}