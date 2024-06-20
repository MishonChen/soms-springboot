package xyz.ontip.service.admin;


import xyz.ontip.pojo.dto.InsertAccountDTO;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.requestVo.InsertAccountVO;
import xyz.ontip.pojo.vo.requestVo.SearchAccountInfo;
import xyz.ontip.pojo.vo.requestVo.UpdateAccountVO;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;


import java.util.List;

public interface UserService {
    List<AccountInfoListVO> getAccountInfoList(AccountInfoListParamVO accountInfoListParamVO);
    void batchDeleteUserByIds(Long[] ids);


    List<AccountInfoListVO>  searchUserInfoList(SearchAccountInfo searchAccountInfo);

    AccountInfoListVO getUserInfoById(Long uId);

    void updateAccountInfo(UpdateAccountVO updateAccountVO);

    void resetPasswordById(Long id);

    void insertUser(InsertAccountDTO insertAccountDTO);
}
