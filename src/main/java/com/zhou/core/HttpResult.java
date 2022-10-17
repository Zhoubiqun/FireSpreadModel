package com.zhou.core;

import java.io.IOException;

public class HttpResult<T> {
    private int code  = 200;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static HttpResult<String> error(IOException e){
        return error(com.zhou.core.HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static HttpResult<String> error(String msg){
        return error(com.zhou.core.HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static HttpResult<String> error(int code, String msg) {
        HttpResult<String> httpResult = new HttpResult<>();
        httpResult.setCode(code);
        httpResult.setMsg(msg);
        return httpResult;
    }

    public static HttpResult<String> ok(String msg){
        HttpResult<String> httpResult = new HttpResult<>();
        httpResult.setMsg(msg);
        return httpResult;
    }

    public static <T> HttpResult<T> ok(T data){
        HttpResult<T> httpResult = new HttpResult<>();
        httpResult.setData(data);
        return httpResult;
    }

    public static HttpResult<String> ok(){
        return new HttpResult<>();
    }
}
