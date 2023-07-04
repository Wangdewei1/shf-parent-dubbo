package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.en.UserLoginStatusLocked;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.LoginVo;
import com.auto.entity.vo.RegisterVo;
import com.auto.mapper.UserInfoMapper;
import com.auto.result.Result;
import com.auto.result.ResultCodeEnum;
import com.auto.service.UserInfoService;
import com.auto.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 用户登录
     * @param loginVo
     * @param
     * @return
     */
    @Override
    public Result login(LoginVo loginVo , UserInfo userInfo) {

        //2.判断userInfo是否为null
        if (userInfo == null){
            //如果为null，则则证明账号不存在
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        //3.判断密码是否正确
        if (!userInfo.getPassword().equals(MD5.encrypt(loginVo.getPassword()))){
            //true，则证明密码不正确
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }

        //4.判断用户是否锁定
        if (userInfo.getStatus() == UserLoginStatusLocked.STATUS_LOCKED.getCode()){
            //如果为ture，则证明账号已经锁定
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }

        //将信息返回给前端
        //登陆成功后，需要在页面显示用户名
        Map<String,Object> respUserInfo = new HashMap<>();
        respUserInfo.put("nickName", userInfo.getNickName());
        respUserInfo.put("phone",userInfo.getPhone());
        return Result.ok(respUserInfo);
    }

    /**
     * 根据手机号
     * @param phone
     * @return
     */
    @Override
    public UserInfo findByPhone(String phone) {
        return userinfoMapper.findByPhone(phone);
    }


}
