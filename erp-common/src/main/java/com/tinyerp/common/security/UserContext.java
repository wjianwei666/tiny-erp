package com.tinyerp.common.security;

import lombok.Data;

@Data
public class UserContext {

    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    private Long userId;
    private String username;
    private String role;

    public static void set(UserContext context) {
        CONTEXT.set(context);
    }

    public static UserContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static Long getUserId() {
        UserContext ctx = get();
        return ctx != null ? ctx.getUserId() : null;
    }

    public static String getUsername() {
        UserContext ctx = get();
        return ctx != null ? ctx.getUsername() : null;
    }

    public static String getRole() {
        UserContext ctx = get();
        return ctx != null ? ctx.getRole() : null;
    }
}