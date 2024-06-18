package xyz.ontip.aspect;

import cn.hutool.jwt.JWTPayload;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.util.JWTUtils;

@Aspect
@Component
@Slf4j
public class PermissionAspect {

    private final HttpServletRequest request;

    @Resource
    public JWTUtils jWTUtils;

    @Autowired
    public PermissionAspect(HttpServletRequest request) {
        this.request = request;
    }


    @Before("@annotation(requirePermission) || @within(requirePermission)")
    public void checkPermission(RequirePermission requirePermission){
        String[] permissions = requirePermission.value();
        // 在这里执行权限检查逻辑
        // 使用permissions进行权限检查
        if (! hasPermission(permissions)) {
            throw new ForbiddenException("权限验证失败");// 没有权限，抛出异常
        }
    }


    private boolean hasPermission(String[] requiredPermissions) {
        // 如果参数为空，则不对权限进行验证
        if (requiredPermissions.length == 0){
            return true;
        }

        // 获取参数
        // 获取会话中的 token
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("accountSessionToken");

        // 从 Cookie 中获取 token
        String cookieToken = getCookieToken(request, sessionToken);
        // 检查会话中的 token
        if (sessionToken != null && jWTUtils.verifyJWT(sessionToken)) {
            JWTPayload jwtPayload = jWTUtils.analysisJWT(sessionToken);
            return verifyRole(jwtPayload, requiredPermissions);
        }
        // 检查 Cookie 中的 token
        if (cookieToken != null && jWTUtils.verifyJWT(cookieToken)) {
            JWTPayload jwtPayload = jWTUtils.analysisJWT(cookieToken);
            return verifyRole(jwtPayload, requiredPermissions);
        }

        return true; // 假设总是有权限
    }

    private boolean verifyRole(JWTPayload jwtPayload, String[] roles) {
        Object jwtRole = jwtPayload.getClaim("role");
        log.info("JwtRole:" + jwtRole);
        if (jwtRole instanceof String) {
            for (String role : roles) {
                if (jwtRole.equals(role)) {
                    return true;
                }
            }
        }
        return false;
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
