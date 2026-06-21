package com.tinyerp.auth.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String username;
    private String realName;
    private String role;
    private String avatar;
}