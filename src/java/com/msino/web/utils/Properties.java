package com.msino.web.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by xudong on 2015-12-6.
 */
@Component
@ConfigurationProperties(locations = "classpath:msino-web.properties",ignoreUnknownFields = false, prefix = "wechat.mowcc")
public class Properties {

    private String token;

    private String encodingAesKey;

    private String appId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
