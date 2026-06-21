package com.tinyerp.common.model;

import com.tinyerp.common.constant.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private int code;
    private String message;
    private T data;
    private long timestamp;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, ResultCode.SUCCESS_MSG, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, ResultCode.SUCCESS_MSG, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS, message, data);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultCode.INTERNAL_ERROR, ResultCode.FAILED_MSG, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.INTERNAL_ERROR, message, null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> unauthorized() {
        return new Result<>(ResultCode.UNAUTHORIZED, ResultCode.UNAUTHORIZED_MSG, null);
    }

    public static <T> Result<T> forbidden() {
        return new Result<>(ResultCode.FORBIDDEN, ResultCode.FORBIDDEN_MSG, null);
    }
}