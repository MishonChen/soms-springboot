package xyz.ontip.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.ontip.pojo.dto.provier.ProviderDTO;
import xyz.ontip.pojo.dto.bill.ProviderMapDTO;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.provider.ProviderParamVO;
import xyz.ontip.pojo.vo.requestVo.provider.SearchProviderVO;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ProviderMapper {
    List<Provider> getAllProvider();

    List<Provider> getSupplierListByPage(@Param("providerParamVO") ProviderParamVO providerParamVO);

    List<Provider> SearchSupplierList(@Param("searchProviderVO") SearchProviderVO searchProviderVO);

    int insertSupplier(@Param("providerDTO") ProviderDTO providerDTO);

    int batchDeleteSupplier(@Param("ids") Long[] ids);

    Provider getSupplierInfoById(Long id);

    int updateSupplierVo(ProviderDTO providerDTO);

    int batchInsertSupplier(@Param("saveExcel") ArrayList<ProviderDTO> providerDTOS);

    List<ProviderMapDTO> getProviderMap();
}
