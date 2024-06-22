package xyz.ontip.controller.admin;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.dto.bill.BillDTO;
import xyz.ontip.pojo.dto.bill.ProviderMapDTO;
import xyz.ontip.pojo.entity.Bill;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.bill.BillPageVO;
import xyz.ontip.pojo.vo.requestVo.bill.InsertBillVO;
import xyz.ontip.pojo.vo.requestVo.bill.SearchBillVO;
import xyz.ontip.pojo.vo.requestVo.bill.UpdateBillVO;
import xyz.ontip.pojo.vo.requestVo.provider.InsertSupplierVO;
import xyz.ontip.pojo.vo.responesVo.bill.BillVO;
import xyz.ontip.service.admin.BillService;
import xyz.ontip.service.admin.ProviderService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bill")
@RequirePermission(value = {"admin"})
public class BillController {

    @Resource
    private BillService billService;
    @Resource
    private ProviderService providerService;


    @PostMapping("/info/all")
    public ResultEntity<?> getBillInfoList() {
        try {
            List<Bill> providers = billService.getAllBill();
            return ResultEntity.success(providers);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/info/page")
    public ResultEntity<?> getBillListByPage(@RequestBody BillPageVO billPageVO) {
        try {
            List<BillVO> providers = billService.getBillListByPage(billPageVO);
            return ResultEntity.success(providers);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/info/{id}")
    public ResultEntity<?> getBillInfoById(@PathVariable Long id) {
        BillDTO billVO = billService.getBillInfoById(id);
        return ResultEntity.success(billVO);
    }

    @PostMapping("/delete/batch")
    public ResultEntity<?> batchDeleteBill(@RequestBody Long[] ids) {
        try {
            billService.batchDeleteBill(ids);
            return ResultEntity.success();
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/insert")
    public ResultEntity<?> insertBill(@RequestBody InsertBillVO insertBillVO) {
        try {
            billService.insertBill(insertBillVO);
            return ResultEntity.success();
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/update")
    public ResultEntity<?> updateBill(@RequestBody UpdateBillVO updateBillVO) {
        try {
            billService.updateBill(updateBillVO);
            return ResultEntity.success();
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/search")
    public ResultEntity<?> searchBill(@RequestBody SearchBillVO searchBillVO) {
        try {
            List<BillVO> billVOS = billService.searchBill(searchBillVO);
            return ResultEntity.success(billVOS);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/provider/map")
    public ResultEntity<?> getProviderMap() {
        List<ProviderMapDTO> providerMapDTOS = providerService.getProviderMap();
        return ResultEntity.success(providerMapDTOS);
    }

    @PostMapping("/export/excel")
    public void exportExcelUserInfo(HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        List<Bill> allBill = billService.getAllBill();
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

    @PostMapping("save/excel")
    public ResultEntity<?> saveExcelUserInfo(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            List<InsertBillVO> maps = reader.readAll(InsertBillVO.class);
            billService.batchInsertBill(maps);
            return ResultEntity.success();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

}
