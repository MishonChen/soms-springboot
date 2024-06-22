package xyz.ontip.aspect;

import cn.hutool.jwt.JWTPayload;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.single.TokenBlockListSingletonMap;
import xyz.ontip.util.JWTUtils;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class PermissionAspect {

    private final HttpServletRequest request;

    @Value("${soms.block.list.token_head}")
    private String tokenHead;
    @Resource
    public JWTUtils jWTUtils;

    @Autowired
    public PermissionAspect(HttpServletRequest request) {
        this.request = request;
    }


    @Before("@annotation(xyz.ontip.annotation.RequirePermission) || @within(xyz.ontip.annotation.RequirePermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法级别的注解
        RequirePermission requirePermission = method.getAnnotation(RequirePermission.class);

        // 如果方法级别没有注解，获取类级别的注解
        if (requirePermission == null) {
            requirePermission = joinPoint.getTarget().getClass().getAnnotation(RequirePermission.class);
        }

        if (requirePermission != null) {
            String[] permissions = requirePermission.value();
            log.info("Checking permissions for: {}", (Object) permissions);
            if (!hasPermission(permissions)) {
                log.warn("Permission check failed for: {}", (Object) permissions);
                throw new ForbiddenException("Cookie获取失败，请同意Cookie隐私政策");
            }
            log.info("Permission check passed for: {}", (Object) permissions);
        }
    }


    private boolean hasPermission(String[] requiredPermissions) {
        // 如果参数为空，则不对权限进行验证
        if (requiredPermissions.length == 0) {
            return true;
        }

        // 获取参数
        // 获取会话中的 token
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("accountSessionToken");
        log.info("sessionToken:" + sessionToken);
        // 从 Cookie 中获取 token
        String cookieToken = getCookieToken(request, sessionToken);
        log.info("cookieToken:" + cookieToken);
        // 检查会话中的 token
        if (sessionToken != null && jWTUtils.verifyJWT(sessionToken)) {
            JWTPayload jwtPayload = jWTUtils.analysisJWT(sessionToken);
            if (verifyRole(jwtPayload, requiredPermissions)) {
                return verifyTokenBlockList(sessionToken);
            }
            return false;
        }

        // 检查 Cookie 中的 token
        if (cookieToken != null && jWTUtils.verifyJWT(cookieToken)) {
            JWTPayload jwtPayload = jWTUtils.analysisJWT(cookieToken);
            if (verifyRole(jwtPayload, requiredPermissions)) {
                return verifyTokenBlockList(cookieToken);
            }
            return false;
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

    private boolean verifyTokenBlockList(String Token) {
        Map<String, Object> tokenMap = TokenBlockListSingletonMap.getInstance();
        return tokenMap == null || tokenMap.get(tokenHead + Token) == null;
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
