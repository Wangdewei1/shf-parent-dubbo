package com.auto.entity.vo;


import com.auto.gruop.ValidatedGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 登录对象
 */
@Data
public class LoginVo implements Serializable {

    private static final Long serialVersionUID = 1L;
    /**
     * 手机号
     */
    @NotBlank(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} , message = "手机号不能为空")
    @Pattern(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} , regexp = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$",message = "手机号不符合规范")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class},message = "密码不能为空")
    @Size(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} ,min = 8,max = 16,message = "密码长度必须在 8 - 16 位")
    private String password;
}
