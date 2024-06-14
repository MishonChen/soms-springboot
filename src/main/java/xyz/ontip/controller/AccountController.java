package xyz.ontip.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.log.Log;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.ontip.constant.HttpMessageConstants;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.entity.Account;
import xyz.ontip.pojo.vo.requestVo.LoginVO;
import xyz.ontip.pojo.vo.requestVo.RegisterVO;
import xyz.ontip.service.AccountService;
import xyz.ontip.util.JWTUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    @Autowired
    private JWTUtils jwtUtils;

    public final static Integer  EXPIRE_DAY = 7;

    @PostMapping("/login")
    public ResultEntity<?> login(@RequestBody LoginVO loginVo, HttpServletResponse response, HttpSession session) {
        try {
            Account account = accountService.login(loginVo);
            String jwt = jwtUtils.createJWT(account, EXPIRE_DAY);

            if (loginVo.getRememberMe()) {
                // 用户选择记住我，创建一个 Cookie
                Cookie cookie = new Cookie("accountToken", jwt);
                log.info(cookie.getValue());
                cookie.setHttpOnly(true); // 防止客户端脚本访问
                cookie.setSecure(false); // 仅在 HTTPS 下传输
                cookie.setPath("/"); // 整个应用程序有效
                cookie.setMaxAge(EXPIRE_DAY * 24 * 60 * 60); // 一周有效期
                response.addCookie(cookie);
            } else {
                // 用户没有选择记住我，保存到会话存储
                String sessionJWT = jwtUtils.createJWT(account, 1);
                session.setAttribute("accountToken", sessionJWT);
            }
            return ResultEntity.success();
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return ResultEntity.failure(HttpMessageConstants.LOGIN_ERROR_CODE,
                    HttpMessageConstants.LOGIN_ERROR_MSG);
        }
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
