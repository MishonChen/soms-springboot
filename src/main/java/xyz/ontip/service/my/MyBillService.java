package xyz.ontip.service.my;

import xyz.ontip.pojo.entity.Bill;
import xyz.ontip.pojo.vo.requestVo.bill.BillPageVO;
import xyz.ontip.pojo.vo.requestVo.bill.SearchBillVO;
import xyz.ontip.pojo.vo.responesVo.bill.BillVO;

import java.util.List;

public interface MyBillService {
    List<Bill> getAllBillById(Long id);

    List<BillVO> getBillListByPage(BillPageVO billPageVO);

    void batchDeleteBill(Long[] ids);

    List<BillVO> searchBill(SearchBillVO searchBillVO);
}
