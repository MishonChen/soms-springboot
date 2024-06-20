package xyz.ontip.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.ontip.pojo.dto.AccountInfoListDTO;
import xyz.ontip.pojo.dto.InsertAccountDTO;
import xyz.ontip.pojo.dto.ResetPasswordDTO;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.requestVo.SearchAccountInfo;
import xyz.ontip.pojo.vo.requestVo.UpdateAccountVO;

import java.util.List;

@Mapper
@Repository("adminUserMapper")
public interface UserMapper {

    List<AccountInfoListDTO> getAccountInfoList(AccountInfoListParamVO accountInfoListParamVO);

    List<AccountInfoListDTO> getAccountInfoListAll();

    int batchDeleteUserByIds(Long[] ids);

    List<AccountInfoListDTO> searchUserInfoList(@Param("searchAccountInfo") SearchAccountInfo searchAccountInfo);

    AccountInfoListDTO getUserInfoById(Long uId);

    int updateAccountInfo(UpdateAccountVO updateAccountVO);

    int resetPasswordById(ResetPasswordDTO resetPasswordDTO);

    int insertUser(@Param("insertAccountDTO") InsertAccountDTO insertAccountDTO);
}
