package com.jsbs.casemall.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails {

    private final Users users;
    private final Map<String, Object> attributes;

    public PrincipalDetails(Users users, Map<String, Object> attributes) {
        this.users = users;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 반환하는 로직을 구현
        return null;
    }

    @Override
    public String getPassword() {
        return users.getUserPw();
    }

    @Override
    public String getUsername() {
        return users.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부를 반환하는 로직을 구현
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부를 반환하는 로직을 구현
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부를 반환하는 로직을 구현
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부를 반환하는 로직을 구현
    }
}
