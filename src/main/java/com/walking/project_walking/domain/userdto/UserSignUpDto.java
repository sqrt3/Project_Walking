package com.walking.project_walking.domain.userdto;

import com.walking.project_walking.domain.Role;
import jakarta.validation.constraints.*;
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

    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "유효한 이메일 주소를 입력하세요."
    )
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./`~])[A-Za-z\\d!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./`~]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 010-1234-5678 형식으로 입력해 주세요.")
    @NotBlank(message = "전화번호를 입력하세요.")
    private String phone;

    @Size(min = 1, max = 15, message = "닉네임은 1자에서 15자 사이여야 합니다.")
    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;

    @NotBlank(message = "성별을 선택하세요.")
    private Boolean gender;

    @NotNull(message = "생년월일을 입력하세요.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birth;

    private Role role;

    private Long loginCount;

    private Integer userLevel;

    private Long userExp;

    private Integer point;

    private Boolean isActive;

    private String profileImage;
}
