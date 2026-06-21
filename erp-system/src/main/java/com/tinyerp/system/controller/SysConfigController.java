package com.tinyerp.system.controller;

import com.tinyerp.common.model.Result;
import com.tinyerp.system.entity.SysConfig;
import com.tinyerp.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "系统配置", description = "系统参数配置管理")
@RestController
@RequestMapping("/api/system/config")
@RequiredArgsConstructor
public class SysConfigController {

    private final SysConfigService configService;

    @Operation(summary = "获取所有配置")
    @GetMapping("/list")
    public Result<List<SysConfig>> list() {
        return Result.success(configService.listAll());
    }

    @Operation(summary = "获取配置")
    @GetMapping("/{key}")
    public Result<SysConfig> getByKey(@PathVariable String key) {
        return Result.success(configService.getByKey(key));
    }

    @Operation(summary = "更新配置")
    @PutMapping
    public Result<Void> update(@RequestBody SysConfig config) {
        configService.updateConfig(config);
        return Result.success();
    }
}