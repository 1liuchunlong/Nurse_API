package com.example.nurse_api.controller;

import com.example.nurse_api.common.COSClientUtil;
import com.example.nurse_api.common.Result;
import com.example.nurse_api.common.ResultVO;
import com.example.nurse_api.pojo.ShareDto;
import com.example.nurse_api.pojo.ShareInfo;
import com.example.nurse_api.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/share")
public class ShareController {
    @Autowired
    private ShareService shareService;

    @Autowired
    private COSClientUtil cosClientUtil;

    @GetMapping("/selectAll")
    public ResultVO selectAll(){
        List<ShareDto> data=shareService.selectAll();
        if(data==null &&data.size()==0){
            return new ResultVO(600,"暂无数据");
        }
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("res",data);
        return new ResultVO(200,hashMap);

    }

    @PostMapping("/insert")
    public ResultVO insert(@RequestPart MultipartFile file,
                           @RequestHeader("Authorization") String sessionkey,
                           @RequestParam String content,
                           @RequestParam String token){

        if(file.isEmpty()){
//            return  Result.FAIL("暂无数据");
            return new ResultVO(600,"暂无数据");
        }

        String filename= cosClientUtil.uploadfile(file);

        if(shareService.innsertInfo(filename,sessionkey,content,token)==0){
            log.info(content);
            return new ResultVO(600,"----------");
        }else {
            return new ResultVO(200,"插入成功");
        }




    }


}
