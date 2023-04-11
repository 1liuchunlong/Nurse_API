package com.example.nurse_api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.nurse_api.common.RedisKey;
import com.example.nurse_api.common.ResultVO;
import com.example.nurse_api.mapper.ShareInfoMapper;
import com.example.nurse_api.pojo.ShareDto;
import com.example.nurse_api.pojo.ShareInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ShareService {
    @Autowired
    private ShareInfoMapper shareInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public List<ShareDto> selectAll(){
        return shareInfoMapper.selectAll();
    }


    public Integer innsertInfo(String filename, String sessionkey, String content,String token) {

        //获取openId
        String josn = redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID +sessionkey);
        if (StringUtils.isBlank(josn)){
            return 0;
        }

        JSONObject jsonObject = JSON.parseObject(josn);

        String open_id = (String) jsonObject.get("openid");

        //获取nickname
        String userjson = redisTemplate.opsForValue().get(RedisKey.TOKEN_KEY +token);
        JSONObject userObject = JSON.parseObject(userjson);
        String nickname =(String) userObject.get("nickname");

        //获取当前日期
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月和小时的格式为两个大写字母
        java.util.Date date = new Date();//获得当前时间
        String create_time = df.format(date);

        //封装属性
        ShareInfo shareInfo=new ShareInfo();
        shareInfo.setOpenId(open_id);
        shareInfo.setNickname(nickname);
        shareInfo.setPicurl(filename);
        shareInfo.setContent(content);
        shareInfo.setFans(0);
        shareInfo.setCreate_time(create_time);
        log.info(shareInfo.toString());

        if(shareInfoMapper.insert(shareInfo)==0)
        {
            return 0;
        }

        return 1;
    }
}
