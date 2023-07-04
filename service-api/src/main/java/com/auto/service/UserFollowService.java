package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.UserFollow;
import com.auto.entity.UserInfo;
import com.auto.result.Result;

public interface UserFollowService extends BaseService<UserFollow> {
    Result findUserFollowByUserIdAndHouseId(UserInfo userInfo, Long houseId);

    UserFollow findUserFollowByUserIdAndHouseId(Long houseId,UserInfo userInfo);
}
