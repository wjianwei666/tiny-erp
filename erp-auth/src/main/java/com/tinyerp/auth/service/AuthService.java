package com.tinyerp.auth.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tinyerp.auth.dto.LoginRequest;
import com.tinyerp.auth.dto.LoginResponse;
import com.tinyerp.auth.entity.SysRole;
import com.tinyerp.auth.entity.SysUser;
import com.tinyerp.auth.entity.SysUserRole;
import com.tinyerp.auth.mapper.SysRoleMapper;
import com.tinyerp.auth.mapper.SysUserMapper;
import com.tinyerp.auth.mapper.SysUserRoleMapper;
import com.tinyerp.common.exception.BusinessException;
import com.tinyerp.common.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, request.getUsername())
        );
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        String role = getUserRole(user.getId());

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        String token = JwtUtil.generateToken(user.getId(), user.getUsername(), role);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRole(role);
        response.setAvatar(user.getAvatar());
        return response;
    }

    private String getUserRole(Long userId) {
        SysUserRole userRole = userRoleMapper.selectOne(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );
        if (userRole == null) {
            return "USER";
        }
        SysRole role = roleMapper.selectById(userRole.getRoleId());
        return role != null ? role.getRoleCode() : "USER";
    }

    @Transactional
    public void register(SysUser user) {
        long count = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, user.getUsername())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        userMapper.insert(user);

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(2L);
        userRoleMapper.insert(userRole);
    }
}