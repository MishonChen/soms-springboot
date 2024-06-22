package xyz.ontip.util;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.ontip.service.token.TokenService;
import xyz.ontip.single.TokenBlockListSingletonList;
import xyz.ontip.single.TokenBlockListSingletonMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 */
@Component
public class ScheduledTasksUtils {

    @Resource
    private TokenService service;
    @Resource
    private JWTUtils jwtUtils;
    @Value("${soms.block.list.token_head}")
    private String tokenHead;

    /**
     * 每天0点0分0秒调用此方法对黑名单进行维护。
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void maintainToken() {
        Map<String, Object> map = TokenBlockListSingletonMap.getInstance();
        List<Object> list = TokenBlockListSingletonList.getInstance();
        int subLength = tokenHead.length();
        List<String> deleteToken = new ArrayList<>();
        for (Object t : list) {
            String token = t.toString().substring(subLength);
            boolean verifyJWT = jwtUtils.verifyJWT(token);
            if (!verifyJWT) {
                deleteToken.add(token);
                map.remove(t);
            }
        }
        service.batchDeleteToken(deleteToken);
    }

}
