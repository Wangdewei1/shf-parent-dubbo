package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.HouseBroker;

import java.util.List;

public interface HouseBrokerService extends BaseService<HouseBroker> {
    List<HouseBroker> findHouseBrokerList(Long houseId);
}
