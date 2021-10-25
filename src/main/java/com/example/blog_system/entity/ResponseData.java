package com.example.blog_system.entity;

/**
 * 前端请求响应的封装类
 *
 */
public class ResponseData<T> {
    private T payload;        //服务器响应数据
    private boolean success; //请求是否成功
    private String msg;       // 错误信息
    private int code = -1;   // 状态码
    private long timestamp; //服务器响应时间

    public ResponseData() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public ResponseData(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
    }

    public ResponseData(boolean success, T payload) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
    }

    public ResponseData(boolean success, T payload, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
        this.code = code;
    }

    public ResponseData(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    public ResponseData(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static ResponseData ok() {
        return new ResponseData(true);
    }

    public static <T> ResponseData ok(T payload) {
        return new ResponseData(true, payload);
    }

    public static <T> ResponseData ok(int code) {
        return new ResponseData(true, null, code);
    }

    public static <T> ResponseData ok(T payload, int code) {
        return new ResponseData(true, payload, code);
    }

    public static ResponseData fail() {
        return new ResponseData(false);
    }

    public static ResponseData fail(String msg) {
        return new ResponseData(false, msg);
    }

    public static ResponseData fail(int code) {
        return new ResponseData(false, null, code);
    }

    public static ResponseData fail(int code, String msg) {
        return new ResponseData(false, msg, code);
    }

}
