package com.tinyerp.common.constant;

public interface ResultCode {

    int SUCCESS = 200;
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int INTERNAL_ERROR = 500;

    String SUCCESS_MSG = "操作成功";
    String FAILED_MSG = "操作失败";
    String UNAUTHORIZED_MSG = "未登录或Token已过期";
    String FORBIDDEN_MSG = "无操作权限";
    String PARAM_ERROR_MSG = "参数校验失败";
}