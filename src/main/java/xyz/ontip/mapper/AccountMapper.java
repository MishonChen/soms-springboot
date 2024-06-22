package xyz.ontip.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.ontip.pojo.dto.account.UpdatePasswordDTO;
import xyz.ontip.pojo.entity.Account;

@Mapper
public interface AccountMapper {
     String getAccountPassword(String username);

     Account getAccountInfo(String username);

     Long verifyRecommendCode(String recommendCode);

     void register(Account account);


    int updatePassword(UpdatePasswordDTO updatePasswordDTO);

    String getPasswordById(Long id);
}
