package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Role;
import com.auto.mapper.AclAdminRoleMapper;
import com.auto.mapper.AclRoleMapper;
import com.auto.service.AclRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 用户管理 业务层
 */
@Service(interfaceClass = AclRoleService.class)
public class AclRoleServiceImpl extends BaseServiceImpl<Role> implements AclRoleService {
    @Autowired
    private AclRoleMapper aclRoleMapper;

    @Autowired
    private AclAdminRoleMapper aclAdminRoleMapper;

    @Override
    public BaseMapper<Role> getBaseMapper() {
        return aclRoleMapper;
    }

    /**
     * 根据用户id查询所有用户列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> findRoleListByAdminId(Long adminId) {
        return aclRoleMapper.findRoleListByAdminId(adminId);
    }

    /**
     * 给用户分配角色
     * @param adminId
     * @return
     */
    @Override
    public Map<String, List<Role>> findUnAssignRoleListAssignRoleListByAdminId(Long adminId) {
/*        //1.查询所有角色列表
        List<Role> allRoleList = aclRoleMapper.findAll();
        //2.查询该用户已经分配的角色id的列表
        List<Long> roleIds = aclAdminRoleMapper.findRoleIdsByAdminId(adminId);
        //3.从自己角色列表中筛选出用户已分配的角色列表
        List<Role> assignRoleList = allRoleList.stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toList());
        //4.从自己角色列表中筛选出用户未分配的角色列表
        List<Role> unAssignRoleList = allRoleList.stream()
                .filter(role -> !roleIds.contains(role.getId()))
                .collect(Collectors.toList());
        //5.将已分配的角色列表 和 未分配的角色列表封装到Map集合中
        Map<String,List<Role>> roleMap = new HashMap<>();
        roleMap.put("assignRoleList",assignRoleList);
        roleMap.put("unAssignRoleList",unAssignRoleList);
        return roleMap;*/

        //1. 查询所有的角色列表
        List<Role> allRoleList = aclRoleMapper.findAll();
        //2. 查询该用户已经分配的角色的id列表
        List<Long> roleIdList = aclAdminRoleMapper.findRoleIdListByAdminId(adminId);
        //3. 从所有角色列表中筛选出用户已分配的角色列表
        List<Role> assignRoleList = allRoleList.stream()
                .filter(role -> roleIdList.contains(role.getId()))
                .collect(Collectors.toList());
        //4. 从所有角色列表中筛选出用户未分配的角色列表
        List<Role> unAssignRoleList = allRoleList.stream()
                .filter(role -> !roleIdList.contains(role.getId()))
                .collect(Collectors.toList());
        //5. 将已分配的角色列表和未分配的角色列表封装到Map中
        Map<String,List<Role>> roleMap = new HashMap<>();
        roleMap.put("assignRoleList",assignRoleList);
        roleMap.put("unAssignRoleList",unAssignRoleList);
        return roleMap;
    }


}
