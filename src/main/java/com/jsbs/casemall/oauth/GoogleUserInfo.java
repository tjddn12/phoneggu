package com.jsbs.casemall.oauth;

import java.util.Map;

public class GoogleUserInfo extends OAuth2UserInfo {

    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    // 구글 OAuth2에 해당하는 데이터가 있는 경우 제공
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
