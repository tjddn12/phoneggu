package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.Role;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        Users user = userRepository.findByEmail(email).orElseGet(() -> {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setUserId(email); // 사용자 ID는 이메일로 설정
            newUser.setUserPw(passwordEncoder.encode("oauth2user")); // 소셜 로그인은 비밀번호가 필요없음
            newUser.setName(name);
            newUser.setPhone(""); // 기본 전화번호 설정
            newUser.setPCode(""); // 기본 우편번호 설정
            newUser.setLoadAddr(""); // 기본 주소 설정
            newUser.setLotAddr(""); // 기본 주소 설정
            newUser.setDetailAddr(""); // 기본 주소 설정
            newUser.setRole(Role.USER);
            return userRepository.save(newUser);
        });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
