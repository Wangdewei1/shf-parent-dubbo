package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.UserFollow;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.UserFollowVo;
import com.auto.result.Result;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    Result findUserFollowByUserIdAndHouseId(UserInfo userInfo, Long houseId);

    UserFollow findUserFollowByUserIdAndHouseId(Long houseId,UserInfo userInfo);

    PageInfo<UserFollowVo> findUserFollowListPage(Integer pageNum, Integer pageSize, Long id);
}
