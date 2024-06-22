package xyz.ontip.service.impl.admin;

import cn.hutool.core.lang.Snowflake;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ontip.mapper.admin.ProviderMapper;
import xyz.ontip.pojo.dto.provier.ProviderDTO;
import xyz.ontip.pojo.dto.bill.ProviderMapDTO;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.provider.InsertSupplierVO;
import xyz.ontip.pojo.vo.requestVo.provider.ProviderParamVO;
import xyz.ontip.pojo.vo.requestVo.provider.SearchProviderVO;
import xyz.ontip.pojo.vo.requestVo.provider.UpdateSupplierVO;
import xyz.ontip.service.admin.ProviderService;
import xyz.ontip.util.JWTUtils;
import xyz.ontip.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProviderServiceImpl implements ProviderService {


    @Resource
    private ProviderMapper providerMapper;
    @Resource
    private Snowflake snowflake;
    @Resource
    private JWTUtils jWTUtils;
    @Resource
    private TimeUtils timeUtils;


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
        } catch (RuntimeException e) {
            log.error("通过分页查询供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Provider> SearchSupplierList(SearchProviderVO searchProviderVO) {
        try {
            List<Provider> providerList = providerMapper.SearchSupplierList(searchProviderVO);
            return providerList;
        } catch (RuntimeException e) {
            log.error("通过关键词查询供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void insertSupplier(InsertSupplierVO insertSupplierVO) {
        ProviderDTO providerDTO = new ProviderDTO();
        BeanUtils.copyProperties(insertSupplierVO, providerDTO);
        providerDTO.setId(snowflake.nextId());
        providerDTO.setCreateId(jWTUtils.getId());
        providerDTO.setModifyId(jWTUtils.getId());
        String stringNowTime = timeUtils.getStringNowTime("yyyy-MM-dd hh:mm:ss");
        providerDTO.setCreationDate(stringNowTime);
        providerDTO.setModifyDate(stringNowTime);
        int row = providerMapper.insertSupplier(providerDTO);
        if (row != 1) {
            throw new RuntimeException("插入供应失败");
        }
    }

    @Transactional
    @Override
    public void deleteSupplier(Long[] pIds) {
        int row = providerMapper.batchDeleteSupplier(pIds);
        if (row != pIds.length) {
            throw new RuntimeException("批量删除供应商失败");
        }
    }

    @Override
    public Provider getSupplierInfoById(Long id) {
        return providerMapper.getSupplierInfoById(id);
    }

    @Transactional
    @Override
    public void updateSupplier(UpdateSupplierVO updateSupplierVO) {
        ProviderDTO providerDTO = new ProviderDTO();
        BeanUtils.copyProperties(updateSupplierVO, providerDTO);
        providerDTO.setModifyId(jWTUtils.getId());
        String stringNowTime = timeUtils.getStringNowTime("yyyy-MM-dd hh:mm:ss");
        providerDTO.setModifyDate(stringNowTime);
        int row = providerMapper.updateSupplierVo(providerDTO);
        if (row != 1) {
            throw new RuntimeException("插入供应失败");
        }
    }

    @Transactional
    @Override
    public void batchInsertSupplier(List<InsertSupplierVO> maps) {
        ArrayList<ProviderDTO> providerDTOS = new ArrayList<>();
        for (InsertSupplierVO insertSupplierVO : maps) {
            ProviderDTO providerDTO = new ProviderDTO();
            BeanUtils.copyProperties(insertSupplierVO, providerDTO);
            providerDTO.setId(snowflake.nextId());
            providerDTO.setCreateId(jWTUtils.getId());
            providerDTO.setModifyId(jWTUtils.getId());
            String stringNowTime = timeUtils.getStringNowTime("yyyy-MM-dd hh:mm:ss");
            providerDTO.setCreationDate(stringNowTime);
            providerDTO.setModifyDate(stringNowTime);
            providerDTOS.add(providerDTO);
        }
        int row = providerMapper.batchInsertSupplier(providerDTOS);
        if (row != maps.size()) {
            throw new RuntimeException("插入供应失败");
        }
    }

    @Override
    public List<ProviderMapDTO> getProviderMap() {
        return providerMapper.getProviderMap();
    }

}
