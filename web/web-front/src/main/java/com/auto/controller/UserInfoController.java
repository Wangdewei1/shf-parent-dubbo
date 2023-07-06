package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.LoginUserInfoConstant;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.LoginVo;
import com.auto.entity.vo.RegisterVo;
import com.auto.result.Result;
import com.auto.service.UserInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

/**
 * 用户相关 功能
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;


    /**
     * 发送验证码
     */
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone){
        return userInfoService.sendCode(phone);
    }

    /**
     * 注册功能
     */
    @PostMapping("/register")
    public Result registerUser(@Validated @RequestBody RegisterVo registerVo){
        return userInfoService.register(registerVo);
    }

    /**
     * 登录功能
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginVo loginVo, HttpSession session){

        //1.根据手机号获取UserInfo对象
        UserInfo userInfo = userInfoService.findByPhone(loginVo.getPhone());

        //将userInfo对象存到session中
        session.setAttribute(LoginUserInfoConstant.LOGIN_USER_INFO, userInfo);

        return userInfoService.login(loginVo,userInfo);
    }

    /**
     * 注销登录
     */
    @GetMapping("/logout")
    public Result logout(HttpSession session){
        //将UserInfo从session中移除
//        session.invalidate();
        session.removeAttribute("UserInfo");
        return Result.ok();
    }

}
