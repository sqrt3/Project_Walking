package com.walking.project_walking.domain;

import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class LoginUser extends Users {
    private final Users user;

    public LoginUser(Users user) {
        super(user.getEmail(), user.getPassword(), user.getRole());
        this.user = user;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Boolean getIsActive() {
        return user.getIsActive();
    }

    public Long getUserId() {
        return this.user.getUserId();
    }
}