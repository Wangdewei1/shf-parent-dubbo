package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.House;

public interface HouseMapper extends BaseMapper<House> {
    Long findHouseCountByCommunityId(Long id);
}
