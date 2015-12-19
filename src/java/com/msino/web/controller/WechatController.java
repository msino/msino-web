package com.msino.web.controller;

import com.msino.web.pojo.WechatCryptMessage;
import com.msino.web.service.WechatService;
import com.msino.web.utils.Logger;
import com.msino.web.utils.MessageUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@Controller
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @RequestMapping(value = {"/wechat/message"}, method = RequestMethod.GET)
    public void login(@RequestParam(value = "signature") String signature,
                        @RequestParam(value = "timestamp") String timestamp,
                        @RequestParam(value = "nonce") String nonce,
                        @RequestParam(value = "echostr") String echostr,
                        HttpServletResponse response) throws IOException {
        Logger.info(this, String.format("signature=%s, timestatmp=%s, nonce=%s, echostr=%s", signature, timestamp, nonce, echostr));
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(echostr);
        out.close();
    }

    @RequestMapping(value = {"/wechat/message"}, method = RequestMethod.POST)
    public void coreJoinPost(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        // 微信加密签名
        String msgSignature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        Logger.info(this, String.format("msgSignature=%s, timestamp=%s, nonce=%s", msgSignature, timestamp, nonce));
        //从请求中读取整个post数据
        InputStream inputStream = request.getInputStream();
        String postData = IOUtils.toString(inputStream, "UTF-8");
        Logger.info(this, "postData:" + postData);
        String msg = wechatService.decryptMsg(msgSignature, timestamp, nonce, postData);
        Logger.info(this, "msg:" + msg);
        // 调用核心业务类接收消息、处理消息
        String respMessage = wechatService.processRequest(msg);
        Logger.info(this, "respMessage:" + respMessage);
        //加密回复消息
        String encryptRspMsg = wechatService.encryptMsg(respMessage, timestamp, nonce);
        Logger.info(this, "encryptRspMsg:" + encryptRspMsg);
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(encryptRspMsg);
        out.close();
    }


}