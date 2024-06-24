package com.jsbs.casemall.oauth;

import java.util.Map;

public class NaverUserInfo extends OAuth2UserInfo {

    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPhone() {
        return (String) attributes.get("phone");
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
