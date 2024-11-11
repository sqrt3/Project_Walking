package com.walking.project_walking.domain.userdto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate {
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./`~])[A-Za-z\\d!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./`~]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @Pattern(
            regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "전화번호는 010-1234-5678 형식으로 입력해 주세요."
    )
    private String phone;

    @Size(min = 1, max = 15, message = "닉네임은 1자에서 15자 사이여야 합니다.")
    private String nickname;
}
