package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.UserFollow;
import org.apache.ibatis.annotations.Param;

/**
 * 关注 持久层
 */
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    UserFollow findUserFollowByUserIdAndHouseId(@Param("userId") Long id,@Param("houseId") Long houseId);
}
