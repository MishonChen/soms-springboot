package xyz.ontip.service.admin;

import xyz.ontip.pojo.dto.bill.BillDTO;
import xyz.ontip.pojo.entity.Bill;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.bill.BillPageVO;
import xyz.ontip.pojo.vo.requestVo.bill.InsertBillVO;
import xyz.ontip.pojo.vo.requestVo.bill.SearchBillVO;
import xyz.ontip.pojo.vo.requestVo.bill.UpdateBillVO;
import xyz.ontip.pojo.vo.responesVo.bill.BillVO;

import java.util.List;

public interface BillService {
    List<Bill> getAllBill();

    List<BillVO> getBillListByPage(BillPageVO billPageVO);

    BillDTO getBillInfoById(Long id);

    void batchDeleteBill(Long[] ids);

    void insertBill(InsertBillVO insertBillVO);

    void updateBill(UpdateBillVO updateBillVO);

    List<BillVO> searchBill(SearchBillVO searchBillVO);

    void batchInsertBill(List<InsertBillVO> maps);
}
