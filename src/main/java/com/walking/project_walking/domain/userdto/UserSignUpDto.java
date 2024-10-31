package com.walking.project_walking.domain.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력하세요.")
    private String phone;

    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;

    @NotBlank(message = "성별을 선택하세요.")
    private Boolean gender;

    @NotBlank(message = "생년월일을 입력하세요.")
    private LocalDate birth;
}
