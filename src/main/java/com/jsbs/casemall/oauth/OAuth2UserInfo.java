package com.jsbs.casemall.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    Map<String, Object> getAttributes();
}
