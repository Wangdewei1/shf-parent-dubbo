package com.auto.util;

/**
 * 生成4位随机验证码
 */
public class VerificationUtil {

    public static String getVerificationCode(){
        String sources = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomNum = (int) (Math.random() * 10);
            stringBuilder.append(sources.charAt(randomNum));
        }
        return stringBuilder.toString();
    }

}
