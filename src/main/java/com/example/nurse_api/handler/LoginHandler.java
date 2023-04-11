package com.example.nurse_api.handler;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import com.alibaba.fastjson.JSON;
import com.example.nurse_api.common.JWTUtils;
import com.example.nurse_api.common.RedisKey;
import com.example.nurse_api.common.Result;
import com.example.nurse_api.pojo.UserDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//拦截器的使用    要放入mvc的配置当中
@Component
public class LoginHandler implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handlerMethod.hasMethodAnnotation(NoAuth.class)){
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            return noLoginResponse(response);
        }
        token = token.replace("Bearer ", "");
        boolean verify = JWTUtils.verify(token);
        if (!verify){
            return noLoginResponse(response);
        }
        String userJson = redisTemplate.opsForValue().get(RedisKey.TOKEN_KEY + token);
        if (StringUtils.isBlank(userJson)){
            return noLoginResponse(response);
        }
        UserDto userDto = JSON.parseObject(userJson, UserDto.class);
        UserThreadLocal.put(userDto);
        return true;
    }

    private boolean noLoginResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.FAIL("未登录")));
        return false;
    }
}