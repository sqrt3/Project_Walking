package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.Users;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;
    private Boolean gender;
    private LocalDate birth;

    public Users toEntity() {
        return Users.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .phone(this.phone)
                .nickname(this.nickname)
                .gender(this.gender)
                .birth(this.birth)
                .build();
    }
}
