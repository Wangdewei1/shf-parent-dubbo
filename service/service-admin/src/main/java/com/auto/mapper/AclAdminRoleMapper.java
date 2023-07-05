package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AclAdminRoleMapper extends BaseMapper<AdminRole> {
    List<Long> findRoleIdsByAdminId(Long adminId);

    void deleteByAdminId(Long adminId);

    void insertBatch(@Param("roleIds") List<Long> roleIds, @Param("adminId") Long adminId);

    List<Long> findRoleIdListByAdminId(Long adminId);
}
