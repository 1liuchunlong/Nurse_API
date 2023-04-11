package com.example.nurse_api.controller;


import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import com.example.nurse_api.common.COSClientUtil;
import com.example.nurse_api.common.Result;
import com.example.nurse_api.common.ResultVO;
import com.example.nurse_api.pojo.User;
import com.example.nurse_api.pojo.WXAuth;
import com.example.nurse_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

        @GetMapping("getSessionId")
    public Result getSessionId(@RequestParam  String code){
        String sessionId = userService.getSessionId(code);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sessionId", sessionId);
        return Result.SUCCESS(hashMap);
    }

    @PostMapping("authLogin")
    public Result authLogin(@RequestBody WXAuth wxAuth) {
        Result result = userService.authLogin(wxAuth);
        log.info("{}",wxAuth);
        return result;
    }

    @GetMapping("userinfo")
    public Result userinfo(@RequestHeader("Authorization") String token
                          ){
            log.info(token);
        return userService.userinfo(token);
    }

    /**
     * 代完善 更新用户信息功能
     * @param file
     *
     * @return
     */
        @PostMapping("/updateUserinfo")
    public Result upcos(@RequestPart MultipartFile file,
                        @RequestHeader("Authorization") String sessionkey,
                        @RequestParam String nickname){

//            MultipartHttpServletRequest multipartRequest =null;
//            if (request instanceof MultipartHttpServletRequest) {
//                multipartRequest = (MultipartHttpServletRequest)(request);
//            }
//            MultipartFile file=multipartRequest.getFile("file");

        if(file.isEmpty()){
//            return  Result.FAIL("暂无数据");
            return Result.FAIL("ZANWUSHUJU");
        }

        String filename= COSClientUtil.uploadfile(file);

        String newToken=userService.updateInfo(nickname, filename,sessionkey);
        log.info("here is newtoken"+newToken);

        HashMap<String,String> map=new HashMap<>();
        map.put("token",newToken);
        return Result.SUCCESS(map);

    }


}
