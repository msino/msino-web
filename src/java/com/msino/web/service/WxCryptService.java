package com.msino.web.service;

import com.msino.web.utils.Logger;
import com.msino.web.utils.Properties;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by xudong on 2015-12-13.
 */
@Service
public class WxCryptService {

    @Autowired
    private Properties properties;

    private WXBizMsgCrypt wxcpt = null;

    @PostConstruct
    public void init() throws AesException {
        wxcpt = new WXBizMsgCrypt(properties.getToken(), properties.getEncodingAesKey(), properties.getAppId());
    }

    public WXBizMsgCrypt getWxBizMsgCrypt() {
        return wxcpt;
    }


}
