package com.example.nurse_api.pojo;

public class ShareDto {


    private String nickname;
    private String picurl;

    private String create_time;
    private int fans;

    private String content;


    public ShareDto(String nickname, String picurl, String create_time, int fans, String content) {
        this.nickname = nickname;
        this.picurl = picurl;
        this.create_time = create_time;
        this.fans = fans;
        this.content = content;
    }

    public ShareDto() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
