package xyz.ontip.service.impl;


import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ontip.pojo.entity.Account;
import xyz.ontip.pojo.vo.requestVo.LoginVO;
import xyz.ontip.mapper.AccountMapper;
import xyz.ontip.pojo.vo.requestVo.RegisterVO;
import xyz.ontip.service.AccountService;

import java.sql.Date;
import java.time.LocalDate;


@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Snowflake snowflake;

    @Override
    public Account login(LoginVO loginVo) {
        String password = accountMapper.getAccountPassword(loginVo.getUsername());
        boolean matchesPassword = passwordEncoder.matches(loginVo.getPassword(), password);
        if (!matchesPassword) {
            throw new RuntimeException("密码错误");
        }
        return accountMapper.getAccountInfo(loginVo.getUsername());
    }

    @Transactional
    @Override
    public void register(RegisterVO registerVO) {
        Account account = new Account();
        long id = snowflake.nextId();
        account.setId(id);
        account.setUsername(registerVO.getUsername());
        account.setPassword(passwordEncoder.encode(registerVO.getPassword()));

        if (registerVO.getRecommendCode() != null && !registerVO.getRecommendCode().isEmpty()) {
            Long recommendId = accountMapper.verifyRecommendCode(registerVO.getRecommendCode());
            if (recommendId > 0) {
                account.setRole("admin");
                account.setRecommendId(recommendId);
            }
        } else {
            account.setRecommendId(null);
            account.setRole("user");
        }
        // 使用java.sql.Date表示SQL的DATE类型
        account.setRegisterTime(Date.valueOf(LocalDate.now()));
        try {
         accountMapper.register(account);
        }catch (Exception e){
            log.warn(e.getMessage());
            throw new RuntimeException("数据插入失败");
        }
    }


}
