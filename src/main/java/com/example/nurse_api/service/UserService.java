package com.example.nurse_api.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.nurse_api.common.JWTUtils;
import com.example.nurse_api.common.RedisKey;
import com.example.nurse_api.common.Result;
import com.example.nurse_api.mapper.UserMapper;
import com.example.nurse_api.pojo.User;
import com.example.nurse_api.pojo.UserDto;
import com.example.nurse_api.pojo.WXAuth;
import com.example.nurse_api.pojo.WxUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
@Slf4j
public class UserService {
    @Autowired
    private WxService wxService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${weixin.secret}")
    private String secret;
    @Value("${weixin.appid}")
    private String appid;
    @Autowired
    private UserMapper userMapper;


    public UserDto login(UserDto userDto) {
        // 登录成功 封装用户信息到token
        userDto.setPassword(null);
        userDto.setUsername(null);
        userDto.setOpenId(null);
        userDto.setWxUnionId(null);
        String token = JWTUtils.sign(userDto.getId());
        userDto.setToken(token);
        //保存到redis内,下次就直接跳过验证
        redisTemplate.opsForValue().set(RedisKey.TOKEN_KEY + token, JSON.toJSONString(userDto), 30, TimeUnit.DAYS);

//        log.info{}
        return userDto;
    }


    public Result register(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        User queryUser = userMapper.selectOne(user.getOpenId());
        if (queryUser == null) {
            userMapper.insert(user);
        }
        //已存在直接登录
        return Result.SUCCESS(login(userDto));
    }

    public String getSessionId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
        url = url.replace("{0}", appid).replace("{1}", secret).replace("{2}", code);
        String res = HttpUtil.get(url);
        String s = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(RedisKey.WX_SESSION_ID + s, res);
        return s;
    }

    public Result authLogin(WXAuth wxAuth) {
        try {
            String wxRes = wxService.wxDecrypt(wxAuth.getEncryptedData(), wxAuth.getSessionId(), wxAuth.getIv());
            WxUserInfo wxUserInfo = JSON.parseObject(wxRes,WxUserInfo.class);


            String json =  redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID + wxAuth.getSessionId());
            JSONObject jsonObject = JSON.parseObject(json);
            String open_id = (String) jsonObject.get("openid");

            wxUserInfo.setOpenId(open_id);

            User user = userMapper.selectOne(wxUserInfo.getOpenId());
            if (user != null) {
                //登录成功

                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(user,userDto);
                return Result.SUCCESS(this.login(userDto));
            } else {
                UserDto userDto = new UserDto();
                userDto.from(wxUserInfo);
                return this.register(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.FAIL();
    }

    public Result userinfo(String token) {
        /**
         * 1. 根据token 来验证此token 是否有效
         * 2. refresh 如果为true 代表刷新 重新生成token和redis里面重新存储 续期
         * 3. false直接返回用户信息 -》 redis中 查询出来 直接返回
         */
//        token = token.replace("Bearer ","");
        boolean verify = JWTUtils.verify(token);
        if (!verify){
            return Result.FAIL("jwt验证未通过");

        }
        String userJson = redisTemplate.opsForValue().get(RedisKey.TOKEN_KEY +token);
        if (StringUtils.isBlank(userJson)){
            System.out.println(RedisKey.TOKEN_KEY+token);
            return Result.FAIL(RedisKey.TOKEN_KEY+token+"未登录");
        }
        UserDto userDto = JSON.parseObject(userJson, UserDto.class);

        return Result.SUCCESS(userDto);
    }

    /**
     * 1.更新头像和昵称
     * 2.然后需要更新token
     * @param nickname
     * @param portrait
     * @param sessionkey
     * @return
     */
    public String updateInfo(String nickname, String portrait, String sessionkey) {
        String josn = redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID +sessionkey);
        if (StringUtils.isBlank(josn)){
            return "error";
        }

        JSONObject jsonObject = JSON.parseObject(josn);

        String open_id = (String) jsonObject.get("openid");
        //更新头像，昵称
        userMapper.update(nickname,portrait,open_id);
        //更新token
        User user=userMapper.selectOne(open_id);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);

        String token = JWTUtils.sign(userDto.getId());
        userDto.setToken(token);
        //保存到redis内,下次就直接跳过验证
        redisTemplate.opsForValue().set(RedisKey.TOKEN_KEY + token, JSON.toJSONString(userDto));


        return token;

    }
}

