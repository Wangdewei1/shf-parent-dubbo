package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.constant.PageConstant;
import com.auto.entity.UserFollow;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.UserFollowVo;
import com.auto.mapper.UserFollowMapper;
import com.auto.result.Result;
import com.auto.service.UserFollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 关注 业务
 */
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService{

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public BaseMapper<UserFollow> getBaseMapper() {
        return userFollowMapper;
    }

    /**
     * 根据用户id 以及 房源id 查询 关注信息
     * @param userInfo
     * @param houseId
     * @return
     */
    @Override
    public Result findUserFollowByUserIdAndHouseId(UserInfo userInfo, Long houseId) {
        /**
         * 根据userId 和 房源id 查询是否关注
         */
        UserFollow userFollow = userFollowMapper.findUserFollowByUserIdAndHouseId(userInfo.getId(),houseId);
        //1.判断关注信息是否存在
        if (userFollow == null) {
            //true，以前没有关注过
            //需要插入一条关注数据
            UserFollow follow = new UserFollow();
            follow.setHouseId(houseId);
            follow.setUserId(userInfo.getId());
            userFollowMapper.insert(follow);
        }else {
            //否则已经关注
            userFollow.setIsDeleted(0);
            userFollowMapper.update(userFollow);
        }
        return Result.ok();
    }

    /**
     * 重载
     * @param houseId
     * @param userInfo
     * @return
     */
    @Override
    public UserFollow findUserFollowByUserIdAndHouseId(Long houseId, UserInfo userInfo) {
        UserFollow userFollow = userFollowMapper.findUserFollowByUserIdAndHouseId(userInfo.getId(),houseId);
        return userFollow;
    }

    /**
     * 根据用户id分页查询关注列表
     * @param pageNum
     * @param pageSize
     * @param id
     * @return
     */
    @Override
    public PageInfo<UserFollowVo> findUserFollowListPage(Integer pageNum, Integer pageSize, Long id) {
        //1.开启分页
        PageHelper.startPage(pageNum, pageSize);
        //2.根据用户id查询关注列表
        List<UserFollowVo> userFollowVoList = userFollowMapper.findUserFollowListPage(id);
        //返回分页结果
        return new PageInfo<>(userFollowVoList, PageConstant.NAVIGATE_PAGES);
    }

    /**
     * 取消关注
     */
    public void delete(UserFollow userFollow) {
        userFollowMapper.delete(userFollow);
    }
}
