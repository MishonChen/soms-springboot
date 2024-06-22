package xyz.ontip.service;

import xyz.ontip.pojo.entity.Account;
import xyz.ontip.pojo.vo.requestVo.LoginVO;
import xyz.ontip.pojo.vo.requestVo.RegisterVO;
import xyz.ontip.pojo.vo.requestVo.account.ResetPasswordVO;

public interface AccountService {

    Account login(LoginVO loginVo);

    void register(RegisterVO registerVO);


    void resetPassword(ResetPasswordVO resetPasswordVO);
}
