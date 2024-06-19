package xyz.ontip.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;
import xyz.ontip.service.admin.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
@RequirePermission(value = {"admin"})
public class UserController {

    @Autowired
    @Qualifier("adminUserService")
    private UserService userService;

    @PostMapping("/account/info")
    public ResultEntity<?> getAccountInfoList(HttpServletRequest request,
                                              @RequestBody AccountInfoListParamVO accountInfoListParamVO) {
       try {
           List<AccountInfoListVO> accountInfoListVOS = userService.getAccountInfoList(accountInfoListParamVO);
           return ResultEntity.success(accountInfoListVOS);
       }catch (Exception e){
           throw new  RuntimeException("发生错误");
       }
    }


    @GetMapping("/test")
    public ResultEntity<?> test() {
        return ResultEntity.success("测试成功");
    }

}
