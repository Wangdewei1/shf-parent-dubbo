package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Admin;
import com.auto.mapper.AclAdminMapper;
import com.auto.service.AclAdminService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = AclAdminService.class)
public class AclAdminServiceImpl extends BaseServiceImpl<Admin> implements AclAdminService {
    @Autowired
    private AclAdminMapper aclAdminMapper;
    @Override
    public BaseMapper<Admin> getBaseMapper() {
        return aclAdminMapper;
    }
}
