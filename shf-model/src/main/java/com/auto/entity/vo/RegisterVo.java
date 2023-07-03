package com.auto.entity.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * 注册对象
 */
@Data
public class RegisterVo implements Serializable {

    public static final Long serialVersionUID = 1L;
    // 昵称
    private String nickName;

    // 手机号
    private String phone;

    // 密码
    private String password;

    // 验证码
    private String code;
}
