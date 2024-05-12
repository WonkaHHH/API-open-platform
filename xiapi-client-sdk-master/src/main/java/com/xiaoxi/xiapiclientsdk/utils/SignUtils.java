package com.xiaoxi.xiapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 *  生成签名工具
 */
public class SignUtils {

    public static String genSign(String body, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String content = body + "." + secretKey;
        return md5.digestHex(content);
    }
}
