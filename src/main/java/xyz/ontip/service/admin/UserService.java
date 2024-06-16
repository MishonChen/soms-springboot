package xyz.ontip.service.admin;


import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;


import java.util.List;

public interface UserService {
    List<AccountInfoListVO> getAccountInfoList(AccountInfoListParamVO accountInfoListParamVO);
}
