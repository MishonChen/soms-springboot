package xyz.ontip.util;

import cn.hutool.jwt.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.pojo.entity.Account;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

@Slf4j
@Component
public class JWTUtils {

    private final HttpServletRequest request;

    public static final String KEY = "Java is the best programming language in the world";

    @Autowired
    public JWTUtils(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @param account    用户信息
     * @param expireTime 过期时间（天）
     */
    public String createJWT(Account account, Integer expireTime) {
        long EXPIRE_DAY = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * expireTime;

        Map<String, Object> map = new HashMap<>() {
            {
                put("uid", account.getId());
                put("role", account.getRole());
                put("expire_time", EXPIRE_DAY);
            }
        };
        return JWTUtil.createToken(map, KEY.getBytes(StandardCharsets.UTF_8));
    }

    public JWTPayload analysisJWT(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayload();
    }

    public Long getId(String token) {
        try {
            if (token != null && !token.isEmpty()) {
                JWTPayload jwtPayload = analysisJWT(token);
                Object uid = jwtPayload.getClaim("uid");
                return Long.parseLong(uid.toString());
            }
        } catch (RuntimeException e) {

            throw new RuntimeException("JWT校验错误");
        }
        throw new RuntimeException("JWT校验错误");
    }

    public Long getId() {
        // 获取参数
        // 获取会话中的 token
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("accountSessionToken");
        log.info("sessionToken:" + sessionToken);
        // 从 Cookie 中获取 token
        String cookieToken = getCookieToken(request, sessionToken);
        log.info("cookieToken:" + cookieToken);
        // 检查会话中的 token
        if (sessionToken != null && this.verifyJWT(sessionToken)) {
            return getId(sessionToken);
        }

        // 检查 Cookie 中的 token
        if (cookieToken != null && this.verifyJWT(cookieToken)) {
            return getId(cookieToken);
        }
        throw new RuntimeException("JWT校验错误");
    }


    public boolean verifyJWT(String token) {
        return JWTUtil.verify(token, KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static String getCookieToken(HttpServletRequest request, String sessionToken) {
        String cookieToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accountCookieToken".equals(cookie.getName())) {
                    cookieToken = cookie.getValue();
                    break;
                }
            }
        }

        // 如果会话和 Cookie 中都没有 token，则验证失败
        if (sessionToken == null && cookieToken == null) {
            throw new ForbiddenException("权限验证失败");
        }
        return cookieToken;
    }
}

