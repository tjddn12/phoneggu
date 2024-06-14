package com.jsbs.casemall.oauth;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> responseAttributes;

    public NaverUserInfo(Map<String, Object> responseAttributes) {
        this.responseAttributes = responseAttributes;
    }

    @Override
    public String getProviderId() {
        return (String) responseAttributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) responseAttributes.get("email");
    }

    @Override
    public String getName() {
        return (String) responseAttributes.get("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return responseAttributes;
    }
}
