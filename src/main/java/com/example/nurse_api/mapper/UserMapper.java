package com.example.nurse_api.mapper;

import com.example.nurse_api.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {

    User selectOne(String openId);
    int insert (User user);



    int update(String nickname, String portrait, String openId);
}
