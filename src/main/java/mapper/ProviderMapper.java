package mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.ontip.pojo.entity.Account;

@Mapper
public interface ProviderMapper {
     String getAccountPassword(String username);

     Account getAccountInfo(String username);

     Long verifyRecommendCode(String recommendCode);

     void register(Account account);


}
