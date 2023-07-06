package com.auto.mapper;

import com.auto.base.BaseService;
import com.auto.entity.Permission;
import com.auto.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AclRolePermissionMapper extends BaseService<RolePermission> {
    List<Permission> findPermissionListByRoleId(Long roleId);

    List<Long> findPermissionIds(Long roleId);

    RolePermission findPermissionListByRoleIdAndPermissionIds(@Param("roleId") Long roleId,@Param("permissionIds") List<Long> removePermissionIds);

    void deleteByRoleId(Long roleId);

    void insertBatch(@Param("roleId") Long roleId,@Param("permissionIds") List<Long> permissionIds);
}
