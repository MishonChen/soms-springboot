package xyz.ontip.aspect;

import cn.hutool.jwt.JWTPayload;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.util.JWTUtils;


@Component
@Aspect
@Slf4j
public class UserControllerAspect {

    @Resource
    private JWTUtils jwtUtils;

    @Pointcut("execution(* xyz.ontip.controller.admin.UserController.*(..))")
    public void userControllerMethods() {}

    @Before("userControllerMethods() && args(request,..)")
    public void verifyAccountRole(HttpServletRequest request) {
        // 获取会话中的 token
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("accountSessionToken");

        // 从 Cookie 中获取 token
        String cookieToken = getCookieToken(request, sessionToken);

        // 检查会话中的 token
        if (sessionToken != null && jwtUtils.verifyJWT(sessionToken)) {
            JWTPayload jwtPayload = jwtUtils.analysisJWT(sessionToken);
            verifyRole(jwtPayload);
        }

        // 检查 Cookie 中的 token
        if (cookieToken != null && jwtUtils.verifyJWT(cookieToken)) {
            JWTPayload jwtPayload = jwtUtils.analysisJWT(cookieToken);
            verifyRole(jwtPayload);
        }
    }

    private static  String getCookieToken(HttpServletRequest request, String sessionToken) {
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

    private void verifyRole(JWTPayload jwtPayload) {
        Object role = jwtPayload.getClaim("role");
        if (role instanceof String roleString) {
            if (!"admin".equals(roleString)) {
                throw new ForbiddenException("权限验证失败");
            }
        } else {
            throw new ForbiddenException("权限验证失败");
        }
    }
}

