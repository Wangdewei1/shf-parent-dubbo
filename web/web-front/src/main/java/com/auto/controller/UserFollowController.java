package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.LoginUserInfoConstant;
import com.auto.entity.UserFollow;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.UserFollowVo;
import com.auto.result.Result;
import com.auto.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 取消关注房源
     */
    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){

        //1.创建UserFollow对象，并更新
        UserFollow userFollow = new UserFollow();
        //2.将关注id设置到新创建的对象 , 删除设置为1
        userFollow.setId(id);
        userFollow.setIsDeleted(1);
        //3.取消关注
        userFollowService.delete(userFollow);
        return Result.ok();
    }
}
