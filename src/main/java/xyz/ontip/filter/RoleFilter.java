package xyz.ontip.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.util.JWTUtils;

import java.io.IOException;

@Component
public class RoleFilter extends OncePerRequestFilter {

    @Resource
    private JWTUtils jwtUtils;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json;;charset=utf-8");
        // 获取url
        String requestURI = request.getRequestURI();
        // 需要管理员权限的
        if (requestURI.startsWith("/api/admin")) {
            // 获取会话中的 token
            HttpSession session = request.getSession();
            String sessionToken = (String) session.getAttribute("accountSessionToken");
            // 从 Cookie 中获取 token
            String cookieToken = getCookieToken(request, sessionToken);
            if (!StrUtil.isBlankIfStr(session)) {
                if (verifyAdminRole(jwtUtils.analysisJWT(sessionToken))) {
                    filterChain.doFilter(request, response); // 继续执行过滤器链
                    return;
                } else {
                    resolver.resolveException(request, response, null, new RuntimeException("你未获得权限"));

                }
            } else {
                if (!StrUtil.isBlankIfStr(cookieToken)) {
                    if (verifyAdminRole(jwtUtils.analysisJWT(cookieToken))) {
                        filterChain.doFilter(request, response); // 继续执行过滤器链
                        return;
                    } else {
                        resolver.resolveException(request, response, null, new RuntimeException("你未获得权限"));
                    }
                }
                resolver.resolveException(request, response, null, new RuntimeException("你未获得权限"));
            }
            return;
        }

        filterChain.doFilter(request, response); // 继续执行过滤器链

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

    private boolean verifyAdminRole(JWTPayload jwtPayload) {
        Object role = jwtPayload.getClaim("role");
        if (role instanceof String roleString) {
            return "admin".equals(roleString);
        } else {
            return false;
        }
    }
}
