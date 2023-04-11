package com.example.nurse_api.pojo;

public class ShareInfo {
    private long shareId;
    private String openId;

    private String nickname;
    private String picurl;

    private String create_time;
    private int fans;

    private String content;

    public ShareInfo(long shareId, String openId, String nickname, String picurl, String create_time, int fans, String content) {
        this.shareId = shareId;
        this.openId = openId;
        this.nickname = nickname;
        this.picurl = picurl;
        this.create_time = create_time;
        this.fans = fans;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "shareId=" + shareId +
                ", openId='" + openId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", picurl='" + picurl + '\'' +
                ", create_time='" + create_time + '\'' +
                ", fans=" + fans +
                ", content='" + content + '\'' +
                '}';
    }

    public ShareInfo() {
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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
