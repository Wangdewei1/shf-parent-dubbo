package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.HouseUser;
import com.auto.mapper.HouseUserMapper;
import com.auto.service.HouseUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 房东 业务层
 */
@Service(interfaceClass = HouseUserService.class)
public class HouseUserServiceImpl extends BaseServiceImpl<HouseUser> implements HouseUserService {

    @Autowired
    private HouseUserMapper houseUserMapper;

    @Override
    public BaseMapper<HouseUser> getBaseMapper() {
        return houseUserMapper;
    }

    /**
     * 根据房源id查询房东列表
     * @param houseId
     * @return
     */
    @Override
    public List<HouseUser> findHouseUserList(Long houseId) {
        return houseUserMapper.findHouseUserList(houseId);
    }
}
