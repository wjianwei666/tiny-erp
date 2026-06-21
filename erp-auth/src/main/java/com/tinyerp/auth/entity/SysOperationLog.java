package com.tinyerp.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;
    private String username;
    private String module;
    private String operation;
    private String description;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String ipAddress;
    private Long costTime;
    private LocalDateTime createTime;
}