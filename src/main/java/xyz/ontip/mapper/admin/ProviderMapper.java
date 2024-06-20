package xyz.ontip.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.provider.ProviderParamVO;
import xyz.ontip.pojo.vo.requestVo.provider.SearchProviderInfo;

import java.util.List;

@Mapper
public interface ProviderMapper {
    List<Provider> getAllProvider();

    List<Provider> getSupplierListByPage(@Param("providerParamVO") ProviderParamVO providerParamVO);

    List<Provider> SearchSupplierList(@Param("searchProviderInfo") SearchProviderInfo searchProviderInfo);
}
