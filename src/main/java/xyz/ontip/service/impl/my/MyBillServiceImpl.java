package xyz.ontip.service.impl.my;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ontip.mapper.my.MyBillMapper;
import xyz.ontip.pojo.dto.bill.BillDTO;
import xyz.ontip.pojo.dto.my.MyBillPageDTO;
import xyz.ontip.pojo.dto.my.MySearchBillDTO;
import xyz.ontip.pojo.entity.Bill;
import xyz.ontip.pojo.vo.requestVo.bill.BillPageVO;
import xyz.ontip.pojo.vo.requestVo.bill.SearchBillVO;
import xyz.ontip.pojo.vo.responesVo.bill.BillVO;
import xyz.ontip.service.my.MyBillService;
import xyz.ontip.util.JWTUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MyBillServiceImpl implements MyBillService {

    @Resource
    private MyBillMapper myBillMapper;
    @Resource
    private JWTUtils jWTUtils;


    @Override
    public List<Bill> getAllBillById(Long id) {
        try {
            List<Bill> bills = myBillMapper.getAllBillById(id);
            return bills;
        } catch (RuntimeException e) {
            log.error("查询全部供应商列表发生错误:{}" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<BillVO> getBillListByPage(BillPageVO billPageVO) {
        try {
            MyBillPageDTO myBillPageDTO = new MyBillPageDTO();
            BeanUtils.copyProperties(billPageVO,myBillPageDTO);
            myBillPageDTO.setId(jWTUtils.getId());
            List<BillDTO> billListByPage = myBillMapper.getBillListByPage(myBillPageDTO);
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

    @Transactional
    @Override
    public void batchDeleteBill(Long[] ids) {
        int row = myBillMapper.batchDeleteBill(ids);
        if (row != ids.length) {
            throw new RuntimeException("订单表批量删除错误");
        }
    }


    @Override
    public List<BillVO> searchBill(SearchBillVO searchBillVO) {
        try {
            MySearchBillDTO mySearchBillDTO = new MySearchBillDTO();
            BeanUtils.copyProperties(searchBillVO,mySearchBillDTO);
            mySearchBillDTO.setId(jWTUtils.getId());
            List<BillDTO> billDTOList = myBillMapper.searchBill(mySearchBillDTO);
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
