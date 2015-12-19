package test;

import java.io.StringReader;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public class Program {

    public static void main(String[] args) throws Exception {

        //
        // 第三方回复公众平台
        //
        String encodingAesKey = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
        String token = "pamtest";
        String appId = "wxb11529c136998cb6";

        for (Provider provider : Security.getProviders()) {
            System.out.println(provider);
        }
        int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
        System.out.println(maxKeyLen);
        maxKeyLen = Cipher.getMaxAllowedKeyLength("AES/CBC/NoPadding");
        System.out.println(maxKeyLen);
        Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
        System.out.println("============");

        // 需要加密的明文
        String timestamp = "1449998763";
        String nonce = "2109834445";
        String msgSignature = "7962869dec486fb12beb36a3d28e5af3001f4a74";
        String enMsg = "<xml>\n" +
                "    <ToUserName><![CDATA[gh_fc98fd6affe8]]></ToUserName>\n" +
                "    <Encrypt><![CDATA[Xzo40sNv9A7aFjXrurvopRy9wrPU8yK+AF4LYVG5poP4GfjaUUdko1lF8gLK/eyLijHzZ7OrpE9cOysg0bvsz+e/Uhx/6pHddFxmNt3ixfJSe2kPJHneO6F0lcVnKV90Tl3BBbQIJj8Z0jICBG2Cwrz4LQJQQ4FUIQD1WjbYF2SWOoQfCUrNvg+5nFzw0Wuw5PLJAFAYCftx2Jvm1ZRMPyj0t3oK17haBv6f7vsQrMP6hCuyWV1WSblj0VtVXqNf+dOGaDyLkWrx2GAH6IKMyiBUzD8e8ymYSgbgMYp8ccZBUp6FUzbedu/V+DHZ6WHQwPHSn2ygEKFl0mZyU5kFhnbYAKVTxouh6RYHaPGzUvgJ+XHqoPOyIP3R0DOJpVWT3zBCM2WKkuq837zZlyvntvMyFWax4Y//GJEdv49neaw=]]></Encrypt>\n" +
                "</xml>";
        String replyMsg = " 中文<xml><ToUserName><![CDATA[oia2TjjewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";

        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        String result = pc.decryptMsg(msgSignature, timestamp, nonce, enMsg);


        System.out.println(result);
    }
}
