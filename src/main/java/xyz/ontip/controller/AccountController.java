package xyz.ontip.controller;

import cn.hutool.core.lang.Snowflake;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.ontip.annotation.RequiresClassPermission;
import xyz.ontip.annotation.RequiresMethodPermission;
import xyz.ontip.constant.HttpMessageConstants;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.entity.Account;
import xyz.ontip.pojo.vo.requestVo.LoginVO;
import xyz.ontip.pojo.vo.requestVo.RegisterVO;
import xyz.ontip.service.AccountService;
import xyz.ontip.util.JWTUtils;



@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private Snowflake snowflake;
    @Resource
    private JWTUtils jwtUtils;

    public final static Integer  EXPIRE_DAY = 7;

    @PostMapping("/login")
    public ResultEntity<?> login(@RequestBody LoginVO loginVo, HttpServletResponse response, HttpSession session) {
        try {
            Account account = accountService.login(loginVo);
            String jwt = jwtUtils.createJWT(account, EXPIRE_DAY);

            if (loginVo.getRememberMe()) {
                // 用户选择记住我，创建一个 Cookie
                Cookie cookie = new Cookie("accountCookieToken", jwt);
                log.info(cookie.getValue());
                // 防止客户端脚本访问
                cookie.setHttpOnly(true);
                // 仅在 HTTPS 下传输
                cookie.setSecure(false);
                // 整个应用程序有效
                cookie.setPath("/");
                // 一周有效期
                cookie.setMaxAge(EXPIRE_DAY * 24 * 60 * 60);
                response.addCookie(cookie);
            } else {
                // 用户没有选择记住我，保存到会话存储
                String sessionJWT = jwtUtils.createJWT(account, 1);
                session.setAttribute("accountSessionToken", sessionJWT);
            }
            return ResultEntity.success();
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return ResultEntity.failure(HttpMessageConstants.LOGIN_ERROR_CODE,
                    HttpMessageConstants.LOGIN_ERROR_MSG);
        }
    }

    @GetMapping("/check-login")
    public ResultEntity<?> checkLogin(HttpServletRequest request) {
        // 获取会话中的 token
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("accountSessionToken");

        // 检查会话中的 token
        if (sessionToken != null && jwtUtils.verifyJWT(sessionToken)) {
            return ResultEntity.success("User is logged in");
        }

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
        // 检查 Cookie 中的 token
        if (cookieToken != null && jwtUtils.verifyJWT(cookieToken)) {
            return ResultEntity.success("User is logged in");
        }

        return ResultEntity.failure(HttpMessageConstants.LOGIN_VERIFY_ERROR_CODE,
                HttpMessageConstants.LOGIN_VERIFY_ERROR_MSG);
    }


    @RequestMapping("/register")
    public ResultEntity<?> register(@RequestBody RegisterVO registerVO) {
        try {
            accountService.register(registerVO);
            return ResultEntity.success();
        } catch (RuntimeException e) {
            return ResultEntity.failure(HttpMessageConstants.REGISTER_ERROR_CODE,
                    HttpMessageConstants.LOGIN_ERROR_MSG);
        }


    }


    @PostMapping("/test")
    public ResultEntity<?> test() {

        return ResultEntity.success(passwordEncoder.encode("123456") + "-sn:" + snowflake.nextId());
    }

}
