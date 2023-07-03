package com.auto.constant;

/**
 * redis 常量值
 */
public class RedisConstant {
    // 默认5分钟
    public static final int DEFAULT_SEND_VERIFICATION_TIME = 60 * 5;
    //默认一分钟
    public static final Long DEFAULT_SEND_VERIFICATION_TIME_MINUTE = 60L;
    //时间
    public static final Long DEFAULT_SEND_VERIFICATION_TIME_INTERVAL = 60L;

    //默认发送验证码次数
    public static final Integer DEFAULT_SEND_VERIFICATION_COUNT = 5;

    //设置默认过期时长 24小时
    public static final int DEFAULT_SEND_VERIFICATION_TIME_EXPIRE = 24*60*60;

    //手机号键常量
    public static final String DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR= ":code:value";

    //冒号
    public static final String DEFAULT_COLON = ":";
}
