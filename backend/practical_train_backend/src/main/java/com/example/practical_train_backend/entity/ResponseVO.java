package com.example.practical_train_backend.entity;




import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;


//@JsonInclude(value = JsonInclude.Include.NON_NULL)
@SuppressWarnings("unused")
public class ResponseVO<T> {

    private final Integer status;

    private final String message;

    private final T data;

    private ResponseVO(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }



    /**
     * 构建返回对象
     *
     * @param status  状态码
     * @param message 消息
     * @param data    数据
     */
    public static <T> ResponseVO<T> build(Integer status, String message, T data) {
        return new ResponseVO<>(status, message, data);
    }

    /**
     * 构建成功返回对象
     *
     * @param message 消息
     */
    public static <T> ResponseVO<T> success(String message) {
        return new ResponseVO<>(200, message, null);
    }

    /**
     * 构建成功返回对象
     *
     * @param data    数据
     * @param message 消息
     */
    public static <T> ResponseVO<T> success(T data, String message) {
        return new ResponseVO<>(200, message, data);
    }
    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(200, null, data);
    }

    public static <T> ResponseVO<T> error(String message) {
        return new ResponseVO<>(500, message, null);
    }
    public static <T> ResponseVO<T> error(T data) {
        return new ResponseVO<>(500, null, data);
    }

    /**
     * 构建成功返回对象
     *
     * @param data    数据
     * @param message 消息
     */
    public static <T> ResponseVO<T> error(T data, String message) {
        return new ResponseVO<>(500, message, data);
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
