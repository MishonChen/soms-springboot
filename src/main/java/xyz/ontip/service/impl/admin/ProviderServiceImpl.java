package xyz.ontip.service.impl.admin;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.ontip.mapper.admin.ProviderMapper;
import xyz.ontip.pojo.entity.Provider;
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
        }catch (RuntimeException e){
            log.error("查询全部供应商列表发生错误:{}"+e.getMessage(),e);
            throw e;
        }
    }
}
