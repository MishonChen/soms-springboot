package xyz.ontip.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;
import xyz.ontip.service.admin.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/account/info")
    public ResultEntity<?> getAccountInfoList(HttpSession session,
                                                   @CookieValue(value = "accountToken", required = false) String cookieToken,
                                                   @RequestBody AccountInfoListParamVO accountInfoListParamVO) {
      List<AccountInfoListVO> accountInfoListVOS = userService.getAccountInfoList(accountInfoListParamVO);
      return ResultEntity.success(accountInfoListVOS);
    }

}
