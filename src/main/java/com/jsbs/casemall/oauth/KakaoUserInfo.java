package com.jsbs.casemall.oauth;

import java.util.Map;

public class KakaoUserInfo extends OAuth2UserInfo {

    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            return null;
        }
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("nickname");
    }

    @Override
    public String getPhone() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            return null;
        }
        return (String) kakaoAccount.get("phone");
    }

    @Override
    public String getPCode() {
        return (String) attributes.get("p_code");
    }

    @Override
    public String getLoadAddr() {
        return (String) attributes.get("loadAddr");
    }

    @Override
    public String getLotAddr() {
        return (String) attributes.get("lotAddr");
    }

    @Override
    public String getDetailAddr() {
        return (String) attributes.get("detailAddr");
    }

    @Override
    public String getExtraAddr() {
        return (String) attributes.get("extraAddr");
    }
}
