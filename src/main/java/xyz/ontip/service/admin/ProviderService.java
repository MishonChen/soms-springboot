package xyz.ontip.service.admin;


import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.provider.ProviderParamVO;
import xyz.ontip.pojo.vo.requestVo.provider.SearchProviderInfo;

import java.util.List;

public interface ProviderService {
    List<Provider> getAllProvider();

    List<Provider> getSupplierListByPage(ProviderParamVO providerParamVO);

    List<Provider> SearchSupplierList(SearchProviderInfo searchProviderInfo);
}
