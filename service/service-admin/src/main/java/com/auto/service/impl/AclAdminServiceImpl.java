package com.auto.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Admin;
import com.auto.mapper.AclAdminMapper;
import com.auto.mapper.AclAdminRoleMapper;
import com.auto.service.AclAdminService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = AclAdminService.class)
public class AclAdminServiceImpl extends BaseServiceImpl<Admin> implements AclAdminService {
    @Autowired
    private AclAdminMapper aclAdminMapper;

    @Autowired
    private AclAdminRoleMapper aclAdminRoleMapper;
    @Override
    public BaseMapper<Admin> getBaseMapper() {
        return aclAdminMapper;
    }

    /**
     * 根据houseId查询不是该房源的经纪人列表
     * @param houseId
     * @return
     */
    @Override
    public List<Admin> findNotAdminListByHouseId(Long houseId) {
        return aclAdminMapper.findNotAdminListByHouseId(houseId);
    }

    /**
     * 分配角色
     * @param roleIds
     * @param adminId
     */
    @Override
    public void assignRole(List<Long> roleIds, Long adminId) {
        //1. 先删除adminId的所有分配记录
        aclAdminRoleMapper.deleteByAdminId(adminId);
        //2. 将roleIds中的所有roleId添加成adminId的新分配记录
        //2.1 判断roleIds是否为空
        if (CollectionUtils.isNotEmpty(roleIds)) {
            aclAdminRoleMapper.insertBatch(roleIds,adminId);
        }
    }
}
