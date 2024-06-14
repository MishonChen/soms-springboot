package xyz.ontip.util;

import cn.hutool.jwt.*;
import xyz.ontip.pojo.entity.Account;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

@Component
public class JWTUtils {

    public static final String KEY = "Java is the best programming language in the world";

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

    public boolean verifyJWT(String token) {
        return JWTUtil.verify(token, KEY.getBytes(StandardCharsets.UTF_8));
    }
}

