package xyz.ontip.controller.admin;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.entity.Provider;
import xyz.ontip.service.admin.ProviderService;

import java.util.List;

@RestController
@RequirePermission(value = {"admin"})
@RequestMapping("/api/admin/provider")
public class ProviderController {


    @Resource
    private ProviderService providerService;


    @PostMapping("/info/all")
    public ResultEntity<?> getProviderInfoList(){
        try {
            List<Provider> providers = providerService.getAllProvider();
            return ResultEntity.success(providers);
        }catch (RuntimeException e){
            return ResultEntity.serverError();
        }
    }


}
