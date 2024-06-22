package xyz.ontip.mapper.my;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.ontip.pojo.dto.bill.BillDTO;
import xyz.ontip.pojo.dto.my.MyBillPageDTO;
import xyz.ontip.pojo.dto.my.MySearchBillDTO;
import xyz.ontip.pojo.entity.Bill;

import java.util.List;

@Mapper
public interface MyBillMapper {
    List<Bill> getAllBillById(Long id);

    List<BillDTO> getBillListByPage(MyBillPageDTO myBillPageDTO);

    int batchDeleteBill(@Param("ids") Long[] ids);

    List<BillDTO> searchBill(MySearchBillDTO mySearchBillDTO);
}
