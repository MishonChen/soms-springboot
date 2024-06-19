package xyz.ontip.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.ontip.pojo.dto.AccountInfoListDto;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.requestVo.SearchUserInfo;

import java.util.List;

@Mapper
@Repository("adminUserMapper")
public interface UserMapper {

   List<AccountInfoListDto> getAccountInfoList(AccountInfoListParamVO accountInfoListParamVO);

    List<AccountInfoListDto> getAccountInfoListAll();
    int batchDeleteUserByIds(Long[] ids);

    List<AccountInfoListDto> searchUserInfoList(@Param("SearchUserInfo") SearchUserInfo searchUserInfo);
}
