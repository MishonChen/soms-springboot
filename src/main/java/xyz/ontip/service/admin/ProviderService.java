package xyz.ontip.service.admin;


import xyz.ontip.pojo.dto.bill.ProviderMapDTO;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.provider.InsertSupplierVO;
import xyz.ontip.pojo.vo.requestVo.provider.ProviderParamVO;
import xyz.ontip.pojo.vo.requestVo.provider.SearchProviderVO;
import xyz.ontip.pojo.vo.requestVo.provider.UpdateSupplierVO;

import java.util.List;

public interface ProviderService {
    List<Provider> getAllProvider();

    List<Provider> getSupplierListByPage(ProviderParamVO providerParamVO);

    List<Provider> SearchSupplierList(SearchProviderVO searchProviderVO);

    void insertSupplier( InsertSupplierVO insertSupplierVO);

    void deleteSupplier(Long[] pIds);

    Provider getSupplierInfoById(Long id);

    void updateSupplier(UpdateSupplierVO updateSupplierVO);

    void batchInsertSupplier(List<InsertSupplierVO> maps);

    List<ProviderMapDTO>  getProviderMap();

}
