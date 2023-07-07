package com.auto.config;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Admin;
import com.auto.entity.Permission;
import com.auto.service.AclAdminService;
import com.auto.service.AclPermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 安全框架
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AclAdminService aclAdminService;

    @Reference
    private AclPermissionService aclPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username){
        //1.根据用户名查询用户信息
        Admin admin = aclAdminService.getUserByUsername(username);
        //2.判断用户是否存在
        if (admin == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        //3.如果用户存在
        //3.1就将用户密码交给springSecurity进行校验出，进行校验处理
        //3.2查询当前用户的所有权限
        List<Permission> permissionList = aclPermissionService.findPermissionListByAdminId(admin.getId());
        //4.因为SpringSecurity中的User 需要集合的参数是 GrantedAuthority
        //4.1所以我们要转成这个集合
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        //4.2判断当前用户的权限列表是否为空
        if (CollectionUtils.isNotEmpty(permissionList)){
            //如果不为空 则映射成一个新的GrantedAuthority集合
            grantedAuthorityList = permissionList.stream()
                    //保留 权限码不为空的权限
                    .filter(permission -> permission.getCode() != null && "".equals(permission.getCode()))
                    .map(permission ->
                         new SimpleGrantedAuthority(permission.getCode())
                    ).collect(Collectors.toList());
        }

//        return new User(username, admin.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        return new User(username, admin.getPassword(), grantedAuthorityList);
    }
}
