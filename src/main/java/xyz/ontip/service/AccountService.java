package xyz.ontip.service;

import xyz.ontip.pojo.entity.Account;
import xyz.ontip.pojo.vo.requestVo.LoginVO;
import xyz.ontip.pojo.vo.requestVo.RegisterVO;

public interface AccountService {

    Account login(LoginVO loginVo);

    void register(RegisterVO registerVO);


}
