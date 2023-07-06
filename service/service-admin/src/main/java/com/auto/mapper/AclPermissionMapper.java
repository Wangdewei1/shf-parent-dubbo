package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.Permission;

import java.util.List;

/**
 * 权限业务相关
 */
public interface AclPermissionMapper extends BaseMapper<Permission> {
    List<Permission> findPermissionListByAdminId(Long adminId);

    Integer permissionCount(Long id);
}
