package com.tinyerp.auth.controller;

import com.tinyerp.auth.dto.LoginRequest;
import com.tinyerp.auth.dto.LoginResponse;
import com.tinyerp.auth.service.AuthService;
import com.tinyerp.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "登录、注册、Token管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success("登录成功", response);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/userinfo")
    public Result<LoginResponse> getUserInfo() {
        return Result.success();
    }
}