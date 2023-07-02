package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.HouseBroker;

import java.util.List;

/**
 * 持久层 经纪人
 */
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {
    List<HouseBroker> findHouseBrokerList(Long houseId);
}
