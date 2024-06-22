package xyz.ontip.service.token;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ontip.mapper.token.TokenMapper;
import xyz.ontip.single.TokenBlockListSingletonList;
import xyz.ontip.single.TokenBlockListSingletonMap;
import xyz.ontip.util.JWTUtils;

import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private TokenMapper tokenMapper;
    @Value("${soms.block.list.token_head}")
    private String tokenHead;
    @Resource
    private JWTUtils jWTUtils;


    @PostConstruct
    public void databaseToService(){
        log.info("Starting databaseToService method");
        Map<String, Object> tokenMap = TokenBlockListSingletonMap.getInstance();
        List<Object> tokenList = TokenBlockListSingletonList.getInstance();
        List<String> tokenLists = tokenMapper.getTokenList();
        for (String token : tokenLists){
            Long id = jWTUtils.getId(String.valueOf(token));
            tokenMap.put(tokenHead + token, id);
            tokenList.add(tokenHead + token);
        }
        log.info("Finished databaseToService method");
    }

    @Transactional
    @Override
    public void batchDeleteToken(List<String> tokens) {
        int row = tokenMapper.batchDeleteToken(tokens);
        if (row != tokens.size()){
            throw new RuntimeException("Token删除失败");
        }
    }

    @Transactional
    @Override
    public void batchInsertToken(List<String> tokens) {
        int row = tokenMapper.batchInsertToken(tokens);
        if (row != tokens.size()){
            throw new RuntimeException("Token插入失败");
        }
    }
}
