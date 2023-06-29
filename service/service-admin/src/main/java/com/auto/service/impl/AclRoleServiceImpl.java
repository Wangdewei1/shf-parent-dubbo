package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Role;
import com.auto.mapper.AclRoleMapper;
import com.auto.service.AclRoleService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 用户管理 业务层
 */
@Service(interfaceClass = AclRoleService.class)
public class AclRoleServiceImpl extends BaseServiceImpl<Role> implements AclRoleService {
    @Autowired
    private AclRoleMapper aclRoleMapper;

    @Override
    public BaseMapper<Role> getBaseMapper() {
        return aclRoleMapper;
    }
}
