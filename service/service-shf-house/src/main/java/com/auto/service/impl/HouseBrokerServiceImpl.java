package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.HouseBroker;
import com.auto.mapper.HouseBrokerMapper;
import com.auto.service.HouseBrokerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 经济人 业务
 */
@Service(interfaceClass = HouseBrokerService.class)
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {
    @Autowired
    private HouseBrokerMapper houseBrokerMapper;
    @Override
    public BaseMapper<HouseBroker> getBaseMapper() {
        return houseBrokerMapper;
    }

    /**
     * 根据房源id查询经纪人列表
     * @param houseId
     * @return
     */
    @Override
    public List<HouseBroker> findHouseBrokerList(Long houseId) {
        return houseBrokerMapper.findHouseBrokerList(houseId);
    }
}
