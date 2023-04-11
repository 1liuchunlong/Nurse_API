package com.example.nurse_api.service;

import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;

public class JDBCDate {

    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月和小时的格式为两个大写字母
        java.util.Date date = new Date();//获得当前时间
        String birthday = df.format(date);
        System.out.println(birthday);

    }
}
