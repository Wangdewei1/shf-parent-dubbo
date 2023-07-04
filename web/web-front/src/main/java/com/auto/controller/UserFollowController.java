package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.LoginUserInfoConstant;
import com.auto.entity.UserInfo;
import com.auto.result.Result;
import com.auto.service.UserFollowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 关注模块
 */
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Reference
    private UserFollowService userFollowService;

    @GetMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId,
                         HttpSession session){

        //1.获取用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute(LoginUserInfoConstant.LOGIN_USER_INFO);

        return userFollowService.findUserFollowByUserIdAndHouseId(userInfo,houseId);
    }
}
