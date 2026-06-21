package com.tinyerp.common.security;

import com.tinyerp.common.constant.SystemConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null && JwtUtil.validateToken(token)) {
            UserContext context = new UserContext();
            context.setUserId(JwtUtil.getUserId(token));
            context.setUsername(JwtUtil.getUsername(token));
            UserContext.set(context);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(SystemConstants.TOKEN_HEADER);
        if (header != null && header.startsWith(SystemConstants.TOKEN_PREFIX)) {
            return header.substring(SystemConstants.TOKEN_PREFIX.length());
        }
        return null;
    }
}