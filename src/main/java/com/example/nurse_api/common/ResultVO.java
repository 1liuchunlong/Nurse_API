package com.example.nurse_api.common;

public class ResultVO {
    private Integer code;//响应码
    private String msg;//响应码
    private Object data;//相应数据

    public ResultVO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(Integer code, Object data) {
        super();
        this.code = code;
        this.data = data;
    }
    public ResultVO() {
        super();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultVO)) return false;

        ResultVO resultVO = (ResultVO) o;

        if (getCode() != null ? !getCode().equals(resultVO.getCode()) : resultVO.getCode() != null) return false;
        if (getMsg() != null ? !getMsg().equals(resultVO.getMsg()) : resultVO.getMsg() != null) return false;
        return getData() != null ? getData().equals(resultVO.getData()) : resultVO.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (getMsg() != null ? getMsg().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

