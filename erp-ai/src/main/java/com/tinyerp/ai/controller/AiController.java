package com.tinyerp.ai.controller;

import com.tinyerp.ai.entity.InventoryAlert;
import com.tinyerp.ai.entity.ReportData;
import com.tinyerp.ai.service.AiService;
import com.tinyerp.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "AI智能服务", description = "AI单据识别、库存预警、智能报表、智能校验")
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @Operation(summary = "AI库存智能预警")
    @GetMapping("/stock/alert")
    public Result<List<InventoryAlert>> getStockAlerts() {
        return Result.success(aiService.getStockAlerts());
    }

    @Operation(summary = "智能报表生成")
    @GetMapping("/report")
    public Result<ReportData> generateReport() {
        return Result.success(aiService.generateReport());
    }

    @Operation(summary = "AI单据智能识别")
    @PostMapping("/ocr/recognize")
    public Result<Map<String, Object>> recognizeDocument(@RequestParam String documentType) {
        return Result.success(aiService.recognizeDocument(documentType));
    }

    @Operation(summary = "智能单据校验")
    @PostMapping("/validate")
    public Result<Map<String, Object>> validateDocument(@RequestParam String documentType,
                                                         @RequestParam Long documentId) {
        return Result.success(aiService.validateDocument(documentType, documentId));
    }
}