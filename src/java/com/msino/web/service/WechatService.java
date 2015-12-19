package com.msino.web.service;

import com.msino.web.pojo.TextMessage;
import com.msino.web.utils.Logger;
import com.msino.web.utils.MessageUtil;
import com.qq.weixin.mp.aes.AesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by xudong on 2015-12-6.
 */
@Service
public class WechatService {

    @Autowired
    private WxCryptService wxCryptService;

    public String decryptMsg(String msgSignature, String timeStamp, String nonce, String postData) {
        String msg = "";
        try {
            //解密消息
            msg = wxCryptService.getWxBizMsgCrypt().decryptMsg(msgSignature, timeStamp, nonce, postData);
        } catch (AesException e) {
            Logger.error(this, "decryptMsg error.", e);
        }
        return msg;
    }

    public String encryptMsg(String replyMsg, String timeStamp, String nonce) {
        String encryptMsg = "";
        try {
            //加密消息
            encryptMsg = wxCryptService.getWxBizMsgCrypt().encryptMsg(replyMsg, timeStamp, nonce);
        } catch (AesException e) {
            Logger.error(this, "encryptMsg error.", e);
        }
        return encryptMsg;
    }

    public String processRequest(String msg) {
        String respMessage = null;
        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";

            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(msg);

            System.out.println("Event==" + requestMap.get("Event"));

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                String content = requestMap.get("Content");
                if ("author".equals(content)) {
                    respContent = "该公众号作者是：苏许栋";
                } else {
                    respContent = "您发送的是文本消息！内容是：" + content;
                }
            }
            // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "您发送的是图片消息！";
            }
            // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
            }
            // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！";
            }
            // 音频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是音频消息！";
            }
            // 视频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
                respContent = "您发送的是视频消息！";
            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 自定义菜单点击事件
                if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_CLICK)) {
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = requestMap.get("EventKey");
                    System.out.println("EventKey=" + eventKey);
                    respContent = "您点击的菜单KEY是" + eventKey;
                }
            }

            textMessage.setContent(respContent);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        } catch (Exception e) {
            respMessage = "有异常了。。。";
            Logger.error(this, "processRequest error.", e);
        }
        return respMessage;
    }
}
