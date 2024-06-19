package xyz.ontip.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.constant.HttpMessageConstants;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.entity.Account;
import xyz.ontip.pojo.vo.requestVo.LoginVO;
import xyz.ontip.pojo.vo.requestVo.RegisterVO;
import xyz.ontip.service.AccountService;
import xyz.ontip.single.TokenBlockListSingletonList;
import xyz.ontip.single.TokenBlockListSingletonMap;
import xyz.ontip.util.JWTUtils;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/account")

public class AccountController {

    @Value("${soms.block.list.token_head}")
    private String tokenHead;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private Snowflake snowflake;
    @Resource
    private JWTUtils jwtUtils;

    public final static Integer EXPIRE_DAY = 7;

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

    @RequirePermission(value = {"admin","user"})
    @GetMapping("/check-login")
    public ResultEntity<?> checkLogin() {
      return ResultEntity.success();
    }


    @PostMapping("/register")
    public ResultEntity<?> register(@RequestBody RegisterVO registerVO) {
        try {
            accountService.register(registerVO);
            return ResultEntity.success();
        } catch (RuntimeException e) {
            return ResultEntity.failure(HttpMessageConstants.REGISTER_ERROR_CODE,
                    HttpMessageConstants.LOGIN_ERROR_MSG);
        }
    }

    @PostMapping("/logout")
    @RequirePermission({"user", "admin"})
    public ResultEntity<?> logout(HttpServletRequest request,HttpServletResponse response) {

        // 获取会话中的 token
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("accountSessionToken");

        // 从 Cookie 中获取 token
        String cookieToken = getCookieToken(request, sessionToken);
        // 检查会话中的 token
        if (sessionToken != null) {
            session.removeAttribute("accountSessionToken");
            return ResultEntity.success();
        }
        // 检查 Cookie 中的 token
        if (cookieToken != null) {
            addTokenBlockList(cookieToken);
            // 删除 cookie
            Cookie cookie = new Cookie("accountCookieToken", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return ResultEntity.success();
        }
        throw new ForbiddenException("登出失败");
    }


    @PostMapping("/test")
    public ResultEntity<?> test() {

        return ResultEntity.success(passwordEncoder.encode("123456") + "-sn:" + snowflake.nextId());
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
        }  // 如果会话和 Cookie 中都没有 token，则验证失败
        if (sessionToken == null && cookieToken == null) {
            throw new ForbiddenException("登出失败");
        }
        return cookieToken;
    }


    private void addTokenBlockList(String Token) {
        if (Token == null){
            return;
        }
        Map<String, Object> tokenMap = TokenBlockListSingletonMap.getInstance();
        List<Object> tokenList = TokenBlockListSingletonList.getInstance();
        // 获取id
        if (tokenMap!= null && tokenMap.get(tokenHead + Token) == null) {
            Long id = jwtUtils.getId(Token);
            tokenMap.put(tokenHead + Token, id);
            tokenList.add(tokenHead + Token);
        }else {
            throw new ForbiddenException("该用户已被注销，请重新登录");
        }
    }

}
