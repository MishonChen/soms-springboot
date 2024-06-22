package xyz.ontip.service.impl;


import cn.hutool.core.lang.Snowflake;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ontip.pojo.dto.account.UpdatePasswordDTO;
import xyz.ontip.pojo.entity.Account;
import xyz.ontip.pojo.vo.requestVo.LoginVO;
import xyz.ontip.mapper.AccountMapper;
import xyz.ontip.pojo.vo.requestVo.RegisterVO;
import xyz.ontip.pojo.vo.requestVo.account.ResetPasswordVO;
import xyz.ontip.service.AccountService;
import xyz.ontip.util.JWTUtils;

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
    @Resource
    private JWTUtils jwtUtils;

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
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException("数据插入失败");
        }
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordVO resetPasswordVO) {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        Long id = jwtUtils.getId();
        updatePasswordDTO.setId(id);
        String passwordById = accountMapper.getPasswordById(id);
        boolean matches = passwordEncoder.matches(resetPasswordVO.getOldPassword(), passwordById);
        if (matches) {
            updatePasswordDTO.setNewPassword(passwordEncoder.encode(resetPasswordVO.getNewPassword()));
            int row = accountMapper.updatePassword(updatePasswordDTO);
            if (row != 1) {
                throw new RuntimeException("重置密码发生错");
            }
        } else {
            throw new RuntimeException("重置密码发生错");
        }
    }

}
