package com.example.nurse_api.common;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

@Component
public class COSClientUtil {


    @Value("${cos.bucketName}")
    private   static String BUCKET_NAME;
    @Value("${cos.regionName}")
    private    static String REGION_NAME;

    //secretId 秘钥id
    @Value("${cos.accessKey}")
    private   static String SECRET_ID ;
    //SecretKey 秘钥

    @Value("${cos.secretKey}")
    private   static String SECRET_KEY ;
    // 腾讯云 自定义文件夹名称
    @Value("${cos.folderPrefix}")
    private    static String PREFIX;


    // 访问域名

    @Value("${cos.url}")
    public    static String URL;
    // 创建COS 凭证

    private   static COSCredentials credentials = new BasicCOSCredentials(SECRET_ID,
            SECRET_KEY);
    // 配置 COS 区域 就购买时选择的区域 我这里是 广州（guangzhou）
    private   static ClientConfig clientConfig = new ClientConfig(new Region(REGION_NAME));



    public static String uploadfile(MultipartFile file) {
        //file校验
        if (file.isEmpty()) {
            return "图像上传失败";
        }
    /*
    防止图片重命名
     */
        //原始图片名
        String originalFilename = file.getOriginalFilename();
        String ext = "." + originalFilename.split("\\.")[1];
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ext;

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);


        COSClient cosClient = new COSClient(credentials, clientConfig);

        File localfile = null;
        try {
            localfile = File.createTempFile("temp", null);
            file.transferTo(localfile);
            String key = "/" + PREFIX + "/" + "_" + year + "_" + month + "_" + day + "_" + fileName;

            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, localfile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

            return URL + key;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    }