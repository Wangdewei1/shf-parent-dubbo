package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.constant.RedisConstant;
import com.auto.en.UserLoginStatusLocked;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.LoginVo;
import com.auto.entity.vo.RegisterVo;
import com.auto.mapper.UserInfoMapper;
import com.auto.result.Result;
import com.auto.result.ResultCodeEnum;
import com.auto.service.UserInfoService;
import com.auto.util.CastUtil;
import com.auto.util.MD5;
import com.auto.util.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户线管逻辑
 */
@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userinfoMapper;

    @Autowired
    private JedisPool jedisPool;

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
        //获取jedis对象
        Jedis jedis = jedisPool.getResource();

        try {
            //校验验证码是否正确

            //1.获取存到redis中的验证码 根据键 手机号
            String checkCode = jedis.get(registerVo.getPhone() + RedisConstant.DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR);
            //判断验证码是否正确
            if (!registerVo.getCode().equals(checkCode)){
                //提示返回错误信息
                return Result.build(null, ResultCodeEnum.CODE_ERROR);
            }
            //2.判断验证码成功后，要删除redis中的验证码
            jedis.del(registerVo.getPhone());
        } finally {
            jedis.close();
        }


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

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @Override
    public Result sendCode(String phone) {
        Jedis jedis = jedisPool.getResource();
        //检查当前手机号是否已经发送过验证码，并且发送时间是否在一分钟之内
        Long ttl = jedis.ttl(phone);
        //判断是否在一分钟之内
        if (ttl > RedisConstant.DEFAULT_SEND_VERIFICATION_TIME - RedisConstant.DEFAULT_SEND_VERIFICATION_TIME_INTERVAL){
            return Result.build(null,ResultCodeEnum.NOT_SEND_MORE_THAN_VERIFICATION_TWICE);
        }
        //拼接当前的年月日
/*        Calendar calendar = Calendar.getInstance();

        String codeCountKey = phone + RedisConstant.DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR +
                RedisConstant.DEFAULT_COLON + calendar.get(Calendar.YEAR) +
                RedisConstant.DEFAULT_COLON + calendar.get(Calendar.MONTH) +
                RedisConstant.DEFAULT_COLON + calendar.get(Calendar.DAY_OF_MONTH);*/

        //拼接当前的年:月:日
        String codeCountKey = phone + RedisConstant.DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR +
                RedisConstant.DEFAULT_COLON + LocalDate.now() //本地服务的当前时间
                .format(DateTimeFormatter.ofPattern(RedisConstant.DEFAULT_JOIN_STR_FORMAT_DATE)); //进行日期格式化

        //检查当前手机号是否发送超过五次 给默认值0
//        int count = CastUtil.castInt(jedis.get(codeCountKey), 0);

        //检查当前手机号今天发送的验证码的次数是否超过5次
        int count = Optional.ofNullable(jedis.get(codeCountKey)) //根据key获取redis中验证码的value
                .map(Integer::valueOf) //将获取的值映射成Integer的ValueOf方法
                .orElse(0); //判断当前的值是否为null ， 如果为null则给默认值0

        //判断当前次数
        if (count == RedisConstant.DEFAULT_SEND_VERIFICATION_COUNT){
            return Result.build(null,ResultCodeEnum.MESSAGE_SEND_MAX_COUNT);
        }

        try {
            //1.获取4位随机验证码
            String verificationCode = VerificationUtil.getVerificationCode();

//            logger.debug(verificationCode);
            //将这个手机号今天你发送的验证码储存到redis中（并做过期处理）

            //发送次数
            jedis.incr(verificationCode);

            //今天第一次发验证码的时候，设置过期时间
            if (count == 0){
                jedis.expire(codeCountKey, RedisConstant.DEFAULT_SEND_VERIFICATION_TIME_EXPIRE);
            }

            //2.将验证码和随机验证码
            //将手机号和验证码存到redis中
            jedis.setex(phone + RedisConstant.DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR, RedisConstant.DEFAULT_SEND_VERIFICATION_TIME, verificationCode);

            return Result.ok();

        } finally {
            jedis.close();
        }
    }

}
