package com.example.nurse_api.handler;

import com.example.nurse_api.pojo.UserDto;

public class UserThreadLocal {

    private static final ThreadLocal<UserDto> LOCAL = new ThreadLocal<>();

    public static void put(UserDto userDto){
        LOCAL.set(userDto);
    }

    public static UserDto get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}