package com.walking.project_walking.domain.userdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {

    @NotBlank
    private String token;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./`~])[A-Za-z\\d!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./`~]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String newPassword;
}
