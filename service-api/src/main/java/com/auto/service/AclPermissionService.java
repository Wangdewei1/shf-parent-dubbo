package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.Permission;
import com.auto.entity.pojo.ResultZNodesInfo;

import java.util.List;

/**
 * 权限 功能
 */
public interface AclPermissionService extends BaseService<Permission> {
    List<Permission> findPermissionListByAdminId(Long adminId);

    List<ResultZNodesInfo> findPermissionListByRoleId(Long roleId);

//    void saveAssignPermissionByRoleIdAndPermissionIds(Long roleId, List<Long> permissionIds);

    void assignPermission(Long roleId, List<Long> permissionIds);
}
