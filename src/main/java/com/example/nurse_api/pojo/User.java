package com.example.nurse_api.pojo;


import java.io.Serializable;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 */
@Data
public class User implements Serializable {

    private Long id;

    private String nickname;

    private String username;

    private String password;

    private String gender;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 背景图片
     */
    private String background;


    private String phoneNumber;

    private String openId;

        private String wxUnionId;

}
