package xyz.ontip.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.constant.HttpMessageConstants;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.requestVo.SearchUserInfo;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;
import xyz.ontip.service.admin.UserService;

import java.util.List;

@Slf4j
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
        } catch (Exception e) {
            throw new RuntimeException("发生错误");
        }
    }

    @PostMapping("/delete/batch/user")
    public ResultEntity<?> batchDeleteUserByIds(@RequestBody Long[] Ids) {
        if (Ids.length > 0) {
            try {
                userService.batchDeleteUserByIds(Ids);
                return ResultEntity.success("批量删除成功");
            } catch (Exception e) {
                log.error(e.getMessage());
                return ResultEntity.failure(HttpMessageConstants.DElETE_USER_ERROR_CODE,
                        HttpMessageConstants.DELETE_USER_ERROR_MSG);
            }
        }
        return ResultEntity.failure(HttpMessageConstants.PARAM_VERIFY_ERROR_CODE,
                HttpMessageConstants.PARAM_VERIFY_ERROR_MSG);
    }

    @PostMapping("/search/user/info/keywords")
    public ResultEntity<?> searchUserInfoList(@RequestBody SearchUserInfo searchUserInfo) {
        try {
            List<AccountInfoListVO> accountInfoListVOS = userService.searchUserInfoList(searchUserInfo);
            return ResultEntity.success(accountInfoListVOS);
        } catch (Exception e) {
            return ResultEntity.serverError();
        }
    }

    @GetMapping("/test")
    public ResultEntity<?> test() {
        return ResultEntity.success("测试成功");
    }

}
