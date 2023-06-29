package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.Role;
import org.springframework.stereotype.Repository;

@Repository //欺骗idea不报错
public interface AclRoleMapper extends BaseMapper<Role> {
}
