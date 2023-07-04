package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.LoginUserInfoConstant;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.UserFollowVo;
import com.auto.result.Result;
import com.auto.service.UserFollowService;
import com.github.pagehelper.PageInfo;
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

    /**
     * 根据房源id和用户id获取房源信息
     * @param houseId
     * @param session
     * @return
     */
    @GetMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId,
                         HttpSession session){

        //1.获取用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute(LoginUserInfoConstant.LOGIN_USER_INFO);
        //2。直接返回业务层结果
        return userFollowService.findUserFollowByUserIdAndHouseId(userInfo,houseId);
    }

    /**
     * 分页查询关注列表
     */
    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable("pageNum") Integer pageNum,
                       @PathVariable("pageSize") Integer pageSize,
                       HttpSession session){

        //1.获取用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute(LoginUserInfoConstant.LOGIN_USER_INFO);

        //2.根据页码和每页显示数以及用户id分页查询关注列表
        PageInfo<UserFollowVo> pageInfo = userFollowService.findUserFollowListPage(pageNum,pageSize,userInfo.getId());
        //3.将分页结果返回给前端
        return Result.ok(pageInfo);
    }
}
