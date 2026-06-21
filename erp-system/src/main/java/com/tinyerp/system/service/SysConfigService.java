package com.tinyerp.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tinyerp.system.entity.SysConfig;
import com.tinyerp.system.mapper.SysConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysConfigService {

    private final SysConfigMapper configMapper;

    public List<SysConfig> listAll() {
        return configMapper.selectList(null);
    }

    public SysConfig getByKey(String configKey) {
        return configMapper.selectOne(
                new LambdaQueryWrapper<SysConfig>()
                        .eq(SysConfig::getConfigKey, configKey)
        );
    }

    public void updateConfig(SysConfig config) {
        SysConfig exist = getByKey(config.getConfigKey());
        if (exist != null) {
            config.setId(exist.getId());
            configMapper.updateById(config);
        } else {
            configMapper.insert(config);
        }
    }
}