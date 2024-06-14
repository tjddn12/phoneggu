package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.Role;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.oauth.GoogleUserInfo;
import com.jsbs.casemall.oauth.OAuth2UserInfo;
import com.jsbs.casemall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    public PrincipalOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 부모 클래스의 loadUser 메서드를 호출하여 OAuth2User 객체를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        // OAuth2 제공자의 이름을 가져옴
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 구글 제공자인 경우 GoogleUserInfo 객체 생성
        if ("google".equals(provider)) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }

        // 사용자 정보가 없으면 예외를 발생시킴
        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_user_info", "사용자 정보를 가져올 수 없습니다", null));
        }

        // 제공자 ID와 이메일을 가져옴
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String socialId = provider + "_" + providerId;

        // 이메일을 기반으로 사용자 정보를 데이터베이스에서 조회
        Optional<Users> optionalUsers = userRepository.findByEmail(email);
        Users users;

        if (optionalUsers.isEmpty()) {
            // 사용자가 없으면 새 사용자 생성
            users = Users.builder()
                    .userId(providerId)
                    .name(oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : "default_name")
                    .email(email)
                    .phone("default_phone")
                    .pCode("default_pCode")
                    .loadAddr("default_loadAddr")
                    .lotAddr("default_lotAddr")
                    .detailAddr("default_detailAddr")
                    .userPw("SOCIAL_LOGIN") // 소셜 로그인 사용자 비밀번호는 기본값 설정
                    .provider(provider)
                    .providerId(providerId)
                    .socialId(socialId)
                    .role(Role.USER) // 기본 역할을 USER로 설정
                    .build();
            // 새 사용자 저장
            userRepository.save(users);
        } else {
            // 사용자가 있으면 기존 사용자 정보를 가져옴
            users = optionalUsers.get();
        }

        // 인증된 OAuth2User 객체를 반환하며, ROLE_USER 권한을 부여함
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email");
    }
}