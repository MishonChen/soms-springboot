package xyz.ontip.controller.admin;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.pojo.vo.requestVo.provider.ProviderParamVO;
import xyz.ontip.pojo.vo.requestVo.provider.SearchProviderInfo;
import xyz.ontip.service.admin.ProviderService;

import java.util.List;

@RestController
@RequirePermission(value = {"admin"})
@RequestMapping("/api/admin/provider")
public class ProviderController {


    @Resource
    private ProviderService providerService;


    @PostMapping("/info/all")
    public ResultEntity<?> getProviderInfoList() {
        try {
            List<Provider> providers = providerService.getAllProvider();
            return ResultEntity.success(providers);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/info/page")
    public ResultEntity<?> getSupplierListByPage(@RequestBody ProviderParamVO providerParamVO) {
        try {
            List<Provider> providers = providerService.getSupplierListByPage(providerParamVO);
            return ResultEntity.success(providers);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/search/info")
    public ResultEntity<?> SearchSupplierList(@RequestBody SearchProviderInfo searchProviderInfo) {
        try {
            List<Provider> providers = providerService.SearchSupplierList(searchProviderInfo);
            return ResultEntity.success(providers);
        } catch (RuntimeException e) {
            return ResultEntity.serverError();
        }
    }

}
