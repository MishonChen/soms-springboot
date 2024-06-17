package xyz.ontip.controller.admin;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ontip.controller.AccountController;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;
import xyz.ontip.service.admin.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class UserController {

    @Autowired
    @Qualifier("adminUserService")
    private UserService userService;
    @Resource
    private AccountController accountController;

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

}
