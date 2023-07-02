package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.HouseUser;

import java.util.List;

/**
 * 房东 业务接口
 */
public interface HouseUserService extends BaseService<HouseUser> {
    List<HouseUser> findHouseUserList(Long houseId);
}
