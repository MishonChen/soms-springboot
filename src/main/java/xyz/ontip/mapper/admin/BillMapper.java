package xyz.ontip.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.ontip.pojo.dto.bill.BillDTO;
import xyz.ontip.pojo.dto.bill.InsertBillDTO;
import xyz.ontip.pojo.dto.bill.UpdateBillDTO;
import xyz.ontip.pojo.entity.Bill;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.bill.BillPageVO;
import xyz.ontip.pojo.vo.requestVo.bill.SearchBillVO;

import java.util.List;

@Mapper
public interface BillMapper {
    List<Bill> getAllBill();

    List<BillDTO> getBillListByPage(BillPageVO billPageVO);

    BillDTO getBillInfoById(Long id);

    int batchDeleteBill(Long[] ids);

    int insertBill(InsertBillDTO insertBillDTO);

    int updateBill(UpdateBillDTO updateBillDTO);

    List<BillDTO> searchBill(SearchBillVO searchBillVO);

    int batchInsertBill(@Param("saveExcel") List<InsertBillDTO> insertBillDTOS);
}
