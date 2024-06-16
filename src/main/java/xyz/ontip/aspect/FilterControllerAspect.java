package xyz.ontip.aspect;

import cn.hutool.jwt.JWTPayload;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import xyz.ontip.util.JWTUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 自定义aop切面
 * 对admin中controller的所有方法进行权限校验
 */
@Aspect
@Component
public class FilterControllerAspect {

    @Resource
    private JWTUtils jwtUtils;

    @Before("execution(* xyz.ontip.controller.admin.*.*(..)) && args(session,cookieToken,..)")
    public void verifyAccountRole(HttpSession session, @CookieValue(value = "accountToken", required = false) String cookieToken) {

        // 获取会话中的 token
        String sessionToken = (String) session.getAttribute("accountToken");

        // 检查会话中的 token
        if (sessionToken == null || !jwtUtils.verifyJWT(sessionToken) || jwtUtils.verifyJWT(cookieToken)) {
            throw new RuntimeException("权限验证失败");
        }
        JWTPayload jwtPayload = jwtUtils.analysisJWT(sessionToken);
        Object role = jwtPayload.getClaim("role");
        if (role instanceof String roleString) {
            if (!"admin".equals(roleString)) {
                throw new RuntimeException("权限验证失败");
            }
        }
        jwtPayload = jwtUtils.analysisJWT(cookieToken);
        role = jwtPayload.getClaim("role");
        if (role instanceof String roleString) {
            if (!"admin".equals(roleString)) {
                throw new RuntimeException("权限验证失败");
            }
        }
    }
}