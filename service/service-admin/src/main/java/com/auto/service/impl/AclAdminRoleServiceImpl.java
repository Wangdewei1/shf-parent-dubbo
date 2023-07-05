package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.AdminRole;
import com.auto.mapper.AclAdminRoleMapper;
import com.auto.service.AclAdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 用户角色业务
 */
@Service(interfaceClass = AclAdminRoleService.class)
public class AclAdminRoleServiceImpl extends BaseServiceImpl<AdminRole> implements AclAdminRoleService {
    @Autowired
    private AclAdminRoleMapper aclAdminRoleMapper;

    @Override
    public BaseMapper<AdminRole> getBaseMapper() {
        return aclAdminRoleMapper;
    }

}
