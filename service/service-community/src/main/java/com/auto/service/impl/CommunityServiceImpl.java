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
}
