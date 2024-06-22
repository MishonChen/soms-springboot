package xyz.ontip.controller.my;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.entity.Bill;
import xyz.ontip.pojo.vo.requestVo.bill.BillPageVO;
import xyz.ontip.pojo.vo.requestVo.bill.SearchBillVO;
import xyz.ontip.pojo.vo.responesVo.bill.BillVO;
import xyz.ontip.service.my.MyBillService;
import xyz.ontip.util.JWTUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/my/bill")
@RequirePermission(value = {"admin","user"})
public class MyBillController {

    @Resource
    private JWTUtils jWTUtils;
    @Autowired
    private MyBillService myBillService;

    @PostMapping("/info/all")
    public ResultEntity<?> getAllBill() {
        Long id = jWTUtils.getId();
        try {
            List<Bill> providers = myBillService.getAllBillById(id);
            return ResultEntity.success(providers);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/info/page")
    public ResultEntity<?> getBillListByPage(@RequestBody BillPageVO billPageVO) {
        try {
            List<BillVO> providers = myBillService.getBillListByPage(billPageVO);
            return ResultEntity.success(providers);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/delete/batch")
    public ResultEntity<?> batchDeleteBill(@RequestBody Long[] ids) {
        try {
            myBillService.batchDeleteBill(ids);
            return ResultEntity.success();
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/search")
    public ResultEntity<?> searchBill(@RequestBody SearchBillVO searchBillVO) {
        try {
            List<BillVO> billVOS = myBillService.searchBill(searchBillVO);
            return ResultEntity.success(billVOS);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/export/excel")
    public void exportExcelUserInfo(HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        List<Bill> allBill = myBillService.getAllBillById(jWTUtils.getId());
        writer.write(allBill, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");

        // 使用URL编码处理文件名
        String fileName = "订单信息.xlsx";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.warn("将用户信息导出到 Excel 时出错: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            writer.close();
        }
    }

}
