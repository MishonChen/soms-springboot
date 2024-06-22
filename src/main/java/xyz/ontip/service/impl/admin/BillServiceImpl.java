package xyz.ontip.service.impl.admin;

import cn.hutool.core.lang.Snowflake;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ontip.mapper.admin.BillMapper;
import xyz.ontip.pojo.dto.bill.BillDTO;
import xyz.ontip.pojo.dto.bill.InsertBillDTO;
import xyz.ontip.pojo.dto.bill.UpdateBillDTO;
import xyz.ontip.pojo.entity.Bill;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.bill.BillPageVO;
import xyz.ontip.pojo.vo.requestVo.bill.InsertBillVO;
import xyz.ontip.pojo.vo.requestVo.bill.SearchBillVO;
import xyz.ontip.pojo.vo.requestVo.bill.UpdateBillVO;
import xyz.ontip.pojo.vo.responesVo.bill.BillVO;
import xyz.ontip.service.admin.BillService;
import xyz.ontip.util.JWTUtils;
import xyz.ontip.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Resource
    private BillMapper billMapper;
    @Resource
    private Snowflake snowflake;
    @Resource
    private JWTUtils jWTUtils;
    @Resource
    private TimeUtils timeUtils;

    @Override
    public List<Bill> getAllBill() {
        try {
            List<Bill> bills = billMapper.getAllBill();
            return bills;
        } catch (RuntimeException e) {
            log.error("查询全部供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<BillVO> getBillListByPage(BillPageVO billPageVO) {
        try {
            List<BillDTO> billListByPage = billMapper.getBillListByPage(billPageVO);
            List<BillVO> voList = new ArrayList<>();
            for (BillDTO billDTO : billListByPage) {
                BillVO billVO = new BillVO();
                BeanUtils.copyProperties(billDTO, billVO);
                billVO.setTotalPrice(billDTO.getTotalPrice() + "元");
                billVO.setProductUnit(billDTO.getProductCount() + billDTO.getProductUnit());
                billVO.setIsPayment(payResult(billDTO.getIsPayment()));
                voList.add(billVO);
            }
            return voList;
        } catch (RuntimeException e) {
            log.error("通过分页查询供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public BillDTO getBillInfoById(Long id) {
        BillDTO billDTO = billMapper.getBillInfoById(id);
        return billDTO;
    }

    @Transactional
    @Override
    public void batchDeleteBill(Long[] ids) {
        int row = billMapper.batchDeleteBill(ids);
        if (row != ids.length) {
            throw new RuntimeException("订单表批量删除错误");
        }
    }

    @Transactional
    @Override
    public void insertBill(InsertBillVO insertBillVO) {
        InsertBillDTO insertBillDTO = new InsertBillDTO();
        BeanUtils.copyProperties(insertBillVO, insertBillDTO);
        insertBillDTO.setBillCode("BILL_" + snowflake.nextId());
        String stringNowTime = timeUtils.getStringNowTime("yyyy-MM-dd hh:mm:ss");
        insertBillDTO.setCreationDate(stringNowTime);
        insertBillDTO.setModifyDate(stringNowTime);
        insertBillDTO.setCreatedBy(jWTUtils.getId());
        insertBillDTO.setModifyBy(jWTUtils.getId());
        int row = billMapper.insertBill(insertBillDTO);
        if (row != 1) {
            throw new RuntimeException("删除订单接口发生错误");
        }
    }

    @Transactional
    @Override
    public void batchInsertBill(List<InsertBillVO> maps) {
        List<InsertBillDTO> insertBillDTOS = new ArrayList<>();
        for (InsertBillVO insertBillVO : maps) {
            InsertBillDTO insertBillDTO = new InsertBillDTO();
            BeanUtils.copyProperties(insertBillVO, insertBillDTO);
            insertBillDTO.setBillCode("BILL_" + snowflake.nextId());
            String stringNowTime = timeUtils.getStringNowTime("yyyy-MM-dd hh:mm:ss");
            insertBillDTO.setCreationDate(stringNowTime);
            insertBillDTO.setModifyDate(stringNowTime);
            insertBillDTO.setCreatedBy(jWTUtils.getId());
            insertBillDTO.setModifyBy(jWTUtils.getId());
            insertBillDTOS.add(insertBillDTO);
        }
        try {
            int row = billMapper.batchInsertBill(insertBillDTOS);
            if (row != maps.size()) {
                throw new RuntimeException("删除订单接口发生错误");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Transactional
    @Override
    public void updateBill(UpdateBillVO updateBillVO) {
        UpdateBillDTO updateBillDTO = new UpdateBillDTO();
        BeanUtils.copyProperties(updateBillVO, updateBillDTO);
        String stringNowTime = timeUtils.getStringNowTime("yyyy-MM-dd hh:mm:ss");
        updateBillDTO.setModifyDate(stringNowTime);
        updateBillDTO.setModifyBy(jWTUtils.getId());
        int row = billMapper.updateBill(updateBillDTO);
        if (row != 1) {
            throw new RuntimeException("删除订单接口发生错误");
        }
    }

    @Override
    public List<BillVO> searchBill(SearchBillVO searchBillVO) {
        try {
            List<BillDTO> billDTOList = billMapper.searchBill(searchBillVO);
            List<BillVO> voList = new ArrayList<>();
            for (BillDTO billDTO : billDTOList) {
                BillVO billVO = new BillVO();
                BeanUtils.copyProperties(billDTO, billVO);
                billVO.setTotalPrice(billDTO.getTotalPrice() + "元");
                billVO.setProductUnit(billDTO.getProductCount() + billDTO.getProductUnit());
                billVO.setIsPayment(payResult(billDTO.getIsPayment()));
                voList.add(billVO);
            }
            return voList;
        } catch (RuntimeException e) {
            log.error("通过分页查询供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    private String payResult(Integer isPayment) {
        if (isPayment == 1) {
            return "已支付";
        } else {
            return "暂未支付";
        }
    }
}
