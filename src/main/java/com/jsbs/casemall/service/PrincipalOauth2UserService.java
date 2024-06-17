package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.Role;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.oauth.GoogleUserInfo;
import com.jsbs.casemall.oauth.NaverUserInfo;
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
import java.util.HashMap;
import java.util.Map;
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
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if ("google".equals(provider)) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if ("naver".equals(provider)) {
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }

        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_user_info", "사용자 정보를 가져올 수 없습니다", null));
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        if (email == null || email.isEmpty()) {
            email = provider + "_" + providerId + "@noemail.com";  // 이메일이 없을 경우 임시 이메일 생성
        }
        String socialId = provider + "_" + providerId;

        Optional<Users> optionalUsers = userRepository.findByEmail(email);
        Users users;

        if (optionalUsers.isEmpty()) {
            users = Users.builder()
                    .userId(providerId)
                    .name(oAuth2UserInfo.getName() != null ? oAuth2UserInfo.getName() : "")
                    .email(email)
                    .phone("") // 기본값 설정
                    .pCode("") // 기본값 설정
                    .loadAddr("") // 기본값 설정
                    .lotAddr("") // 기본값 설정
                    .detailAddr("") // 기본값 설정
                    .userPw("SOCIAL_LOGIN") // 소셜 로그인 사용자 비밀번호는 기본값 설정
                    .provider(provider)
                    .providerId(providerId)
                    .socialId(socialId)
                    .role(Role.USER) // 기본 역할을 USER로 설정
                    .build();
            userRepository.save(users);
        } else {
            users = optionalUsers.get();
        }

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("socialId", socialId);  // socialId를 attributes에 추가

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "socialId");  // socialId를 기본 속성으로 설정
    }
}
