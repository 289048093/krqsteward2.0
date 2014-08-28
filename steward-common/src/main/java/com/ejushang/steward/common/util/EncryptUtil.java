package com.ejushang.steward.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * User: Baron.Zhang
 * Date: 2014/7/21
 * Time: 14:21
 */
public class EncryptUtil {

    public static final String MD5_SIGN = "md5";
    public static final String SHA_1_SIGN = "sha-1";
    public static final String ENCODING_UTF8 = "UTF8";

    /**
     * md5加密
      * @param inputText
     * @return
     */
    public static String md5(String inputText) {
        return encrypt(inputText, MD5_SIGN);
    }

    /**
     * sha-1加密
     * @param inputText
     * @return
     */
    public static String sha(String inputText) {
        return encrypt(inputText, SHA_1_SIGN);
    }

    /**
     * md5或者sha-1加密
     *
     * @param inputText
     *            要加密的内容
     * @param algorithmName
     *            加密算法名称：md5或者sha-1，不区分大小写
     * @return
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = MD5_SIGN;
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes(ENCODING_UTF8));
            byte s[] = m.digest();
            // m.digest(inputText.getBytes("UTF8"));
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    // 返回十六进制字符串
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
                    3));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        /*//md5加密测试
        String md5_1 = md5("123");
        String md5_2 = md5("abc");
        System.out.println(md5_1 + "\n" + md5_2);
        System.out.println("md5 length: " + md5_1.length());
        //sha加密测试
        String sha_1 = sha("123");
        String sha_2 = sha("abc");
        System.out.println(sha_1 + "\n" + sha_2);
        System.out.println("sha length: " + sha_1.length());*/

        String zyAppKey = md5("zhikucheng");
        String zyAppSecret = md5("zhikucheng123");
        String yylAccessToken = md5("yiyingligongfang");

        System.out.println("ZY_APP_KEY : " + zyAppKey);
        System.out.println("ZY_APP_SECRET : " + zyAppSecret);
        System.out.println("YYL_ACCESS_TOKEN : " + yylAccessToken);
    }
}
