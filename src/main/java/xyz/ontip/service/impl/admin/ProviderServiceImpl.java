package xyz.ontip.service.impl.admin;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.ontip.mapper.admin.ProviderMapper;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.provider.ProviderParamVO;
import xyz.ontip.pojo.vo.requestVo.provider.SearchProviderInfo;
import xyz.ontip.service.admin.ProviderService;

import java.util.List;

@Slf4j
@Service
public class ProviderServiceImpl implements ProviderService {


    @Resource
    private ProviderMapper providerMapper;


    @Override
    public List<Provider> getAllProvider() {
        try {
            List<Provider> providerList = providerMapper.getAllProvider();
            return providerList;
        } catch (RuntimeException e) {
            log.error("查询全部供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Provider> getSupplierListByPage(ProviderParamVO providerParamVO) {
        try {
            return providerMapper.getSupplierListByPage(providerParamVO);
        }catch (RuntimeException e){
            log.error("通过分页查询供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Provider> SearchSupplierList(SearchProviderInfo searchProviderInfo) {
        try {
            List<Provider> providerList = providerMapper.SearchSupplierList(searchProviderInfo);
            return providerList;
        }catch (RuntimeException e){
            log.error("通过关键词查询供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }
}
