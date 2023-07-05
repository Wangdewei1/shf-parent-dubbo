package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.Permission;

import java.util.List;

/**
 * 权限 功能
 */
public interface AclPermissionService extends BaseService<Permission> {
    List<Permission> findPermissionListByAdminId(Long adminId);
}
