package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Community;
import com.auto.mapper.CommunityMapper;
import com.auto.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {
    @Autowired
    private CommunityMapper communityMapper;

    @Override
    public BaseMapper<Community> getBaseMapper() {
        return communityMapper;
    }

    /**
     * 重新小区  判断是否有房源
     * @param id
     */
//    @Override
//    public void delete(Long id) {
//        //1.判断当前小区是否有房源
//
//        Long houseCount = communityMapper.findHouseCountByCommunityId(id);
//
//        //2.如果有房源则不能删除
//        if (houseCount > 0){
//            throw new RuntimeException("community can not delete!!!");
//        }
//
//        super.delete(id);
//    }
}
