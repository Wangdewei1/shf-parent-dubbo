package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Permission;
import com.auto.helper.PermissionHelper;
import com.auto.mapper.AclPermissionMapper;
import com.auto.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 菜单管理
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private AclPermissionMapper aclPermissionMapper;

    @Override
    public BaseMapper<Permission> getBaseMapper() {
        return aclPermissionMapper;
    }

    /**
     * 重写findAl方法
     * @return
     */
    @Override
    public List<Permission> findAll() {
        List<Permission> permissionList = aclPermissionMapper.findAll();
        return PermissionHelper.build(permissionList);
    }
}
