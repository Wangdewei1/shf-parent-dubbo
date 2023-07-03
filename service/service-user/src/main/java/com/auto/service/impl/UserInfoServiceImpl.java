package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.RegisterVo;
import com.auto.mapper.UserInfoMapper;
import com.auto.result.Result;
import com.auto.result.ResultCodeEnum;
import com.auto.service.UserInfoService;
import com.auto.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户线管逻辑
 */
@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userinfoMapper;

    @Override
    public BaseMapper<UserInfo> getBaseMapper() {
        return userinfoMapper;
    }

    /**
     * 注册用户
     * @param registerVo
     * @return
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    @Override
    public Result register(RegisterVo registerVo) {

        //校验手机号是否别注册
        UserInfo userInfo = userinfoMapper.findByPhone(registerVo.getPhone());
        if (userInfo != null){
            //如果为空，则说明手机号已经被注册了
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //校验名称是否被注册
        userInfo = userinfoMapper.findByNickName(registerVo.getNickName());
        if (userInfo != null){
            //如果为空，则说明名称已经被注册了
            return Result.build(null,ResultCodeEnum.NICK_NAME_REGISTER_ERROR);
        }

        //给密码MD5加密
        registerVo.setPassword(MD5.encrypt(registerVo.getPassword()));

        userinfoMapper.register(registerVo);

        return Result.ok();
    }
}
