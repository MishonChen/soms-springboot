package xyz.ontip.controller.admin;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.ontip.annotation.RequirePermission;
import xyz.ontip.constant.HttpMessageConstants;
import xyz.ontip.exception.ForbiddenException;
import xyz.ontip.pojo.ResultEntity;
import xyz.ontip.pojo.dto.InsertAccountDTO;
import xyz.ontip.pojo.vo.requestVo.AccountInfoListParamVO;
import xyz.ontip.pojo.vo.requestVo.InsertAccountVO;
import xyz.ontip.pojo.vo.requestVo.SearchAccountInfo;
import xyz.ontip.pojo.vo.requestVo.UpdateAccountVO;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;
import xyz.ontip.service.admin.UserService;
import xyz.ontip.util.JWTUtils;
import xyz.ontip.util.ScheduledTasksUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequirePermission(value = {"admin"})
public class UserController {

    @Autowired
    @Qualifier("adminUserService")
    private UserService userService;
    @Resource
    private JWTUtils jWTUtils;
    @Autowired
    private Snowflake snowflake;


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
    public ResultEntity<?> searchUserInfoList(@RequestBody SearchAccountInfo searchAccountInfo) {
        try {
            List<AccountInfoListVO> accountInfoListVOS = userService.searchUserInfoList(searchAccountInfo);
            return ResultEntity.success(accountInfoListVOS);
        } catch (Exception e) {
            return ResultEntity.serverError();
        }
    }

    @GetMapping("/account/info/{id}")
    public ResultEntity<?> GetUserInfoById(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ResultEntity.failure(HttpMessageConstants.PARAM_VERIFY_ERROR_CODE,
                    HttpMessageConstants.PARAM_VERIFY_ERROR_MSG);
        }
        try {
            Long uId = Long.parseLong(id);
            AccountInfoListVO accountInfoListVO = userService.getUserInfoById(uId);
            return ResultEntity.success(accountInfoListVO);
        } catch (NumberFormatException e) {
            log.warn("类型转换异常");
            return ResultEntity.failure(HttpMessageConstants.PARAM_VERIFY_ERROR_CODE,
                    HttpMessageConstants.PARAM_VERIFY_ERROR_MSG);
        }
    }

    @PostMapping("/account/update")
    public ResultEntity<?> updateAccountInfo(@RequestBody UpdateAccountVO updateAccountVO) {
        try {
            userService.updateAccountInfo(updateAccountVO);
            return ResultEntity.success();
        } catch (Exception e) {
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/account/reset/password/{id}")
    public ResultEntity<?> resetPasswordById(@PathVariable Long id) {
        try {
            userService.resetPasswordById(id);
            return ResultEntity.success();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/account/insert")
    public ResultEntity<?> insertUser(HttpServletRequest request, @RequestBody InsertAccountVO insertAccountVO) {
        // 获取会话中的 token
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("accountSessionToken");

        // 从 Cookie 中获取 token
        String cookieToken = getCookieToken(request, sessionToken);
        // 检查会话中的 token
        Long recommendId;
        InsertAccountDTO insertAccountDTO = new InsertAccountDTO();
        BeanUtils.copyProperties(insertAccountVO, insertAccountDTO);
        if (sessionToken != null && jWTUtils.verifyJWT(sessionToken)) {
            recommendId = jWTUtils.getId(sessionToken);
            insertAccountDTO.setRecommendId(recommendId);
        }
        // 检查 Cookie 中的 token
        if (cookieToken != null && jWTUtils.verifyJWT(cookieToken)) {
            recommendId = jWTUtils.getId(cookieToken);
            insertAccountDTO.setRecommendId(recommendId);
        }
        insertAccountDTO.setId(snowflake.nextId());
        try {
            userService.insertUser(insertAccountDTO);
            return ResultEntity.success();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResultEntity.serverError();
        }
    }

    @PostMapping("/account/export/excel")
    public void exportExcelUserInfo(HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        List<AccountInfoListVO> accountInfoListVOS = userService.getAllUserInfo();
        writer.write(accountInfoListVOS, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");

        // 使用URL编码处理文件名
        String fileName = "用户信息.xlsx";
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





    @GetMapping("/test")
    public ResultEntity<?> test() {
        return ResultEntity.success("测试成功");
    }


    private static String getCookieToken(HttpServletRequest request, String sessionToken) {
        String cookieToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accountCookieToken".equals(cookie.getName())) {
                    cookieToken = cookie.getValue();
                    break;
                }
            }
        }

        // 如果会话和 Cookie 中都没有 token，则验证失败
        if (sessionToken == null && cookieToken == null) {
            throw new ForbiddenException("权限验证失败");
        }
        return cookieToken;
    }
}
