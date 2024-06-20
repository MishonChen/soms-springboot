package xyz.ontip.service.admin;


import xyz.ontip.pojo.dto.InsertAccountDTO;
import xyz.ontip.pojo.vo.requestVo.account.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.requestVo.account.SaveExcelUserInfoVO;
import xyz.ontip.pojo.vo.requestVo.account.SearchAccountInfo;
import xyz.ontip.pojo.vo.requestVo.account.UpdateAccountVO;
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

    List<AccountInfoListVO> getAllUserInfo();

    void batchSaveUser(List<SaveExcelUserInfoVO> maps);
}
