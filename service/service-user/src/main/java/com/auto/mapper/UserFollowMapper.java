package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.UserFollow;
import com.auto.entity.vo.UserFollowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关注 持久层
 */
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    UserFollow findUserFollowByUserIdAndHouseId(@Param("userId") Long id,@Param("houseId") Long houseId);

    List<UserFollowVo> findUserFollowListPage(@Param("userId") Long id);
}
