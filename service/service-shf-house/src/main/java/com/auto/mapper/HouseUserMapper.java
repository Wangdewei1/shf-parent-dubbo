package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.HouseUser;

import java.util.List;

/**
 * 房东 持久层
 */
public interface HouseUserMapper extends BaseMapper<HouseUser> {
    List<HouseUser> findHouseUserList(Long houseId);
}
