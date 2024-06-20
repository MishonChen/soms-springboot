package xyz.ontip.service.impl.admin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ontip.mapper.admin.UserMapper;
import xyz.ontip.pojo.dto.AccountInfoListDTO;
import xyz.ontip.pojo.dto.InsertAccountDTO;
import xyz.ontip.pojo.dto.ResetPasswordDTO;
import xyz.ontip.pojo.dto.SaveExcelUserInfoDTO;
import xyz.ontip.pojo.vo.requestVo.*;
import xyz.ontip.pojo.vo.responesVo.AccountInfoListVO;
import xyz.ontip.service.admin.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("adminUserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${soms.reset.password.default}")
    private String defaultPassword;
    @Resource
    private Snowflake snowflake;

    @Override
    public List<AccountInfoListVO> getAccountInfoList(AccountInfoListParamVO accountInfoListParamVO) {
        try {
            List<AccountInfoListDTO> accountInfoList;
            if (accountInfoListParamVO.getPageSize() == 0 && accountInfoListParamVO.getPageSizeIndex() == 0) {
                accountInfoList = userMapper.getAccountInfoListAll();
            } else {
                accountInfoList = userMapper.getAccountInfoList(accountInfoListParamVO);
            }
            List<AccountInfoListVO> accountInfoListVOS = new ArrayList<>();
            for (AccountInfoListDTO accountInfoListDto : accountInfoList) {
                AccountInfoListVO accountInfoListVO = new AccountInfoListVO();
                BeanUtils.copyProperties(accountInfoListDto, accountInfoListVO);
                accountInfoListVO.setRegisterTime(DateUtil.format(accountInfoListDto.getRegisterTime(), "yyyy-MM-dd"));
                switch (accountInfoListDto.getRole()) {
                    case "admin" -> accountInfoListVO.setRole("管理员");
                    default -> accountInfoListVO.setRole("普通用户");
                }
                accountInfoListVOS.add(accountInfoListVO);
            }
            return accountInfoListVOS;
        } catch (Exception e) {
            throw new RuntimeException("发生异常，请联系管理员");
        }
    }

    @Transactional
    @Override
    public void batchDeleteUserByIds(Long[] ids) {
        try {
            int deleteRow = userMapper.batchDeleteUserByIds(ids);
            if (deleteRow != ids.length) {
                throw new RuntimeException("删除错误");
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException("删除错误");
        }
    }

    @Override
    public List<AccountInfoListVO> searchUserInfoList(SearchAccountInfo searchAccountInfo) {
        List<AccountInfoListDTO> accountInfoListDTOS = userMapper.searchUserInfoList(searchAccountInfo);
        List<AccountInfoListVO> accountInfoListVOS = new ArrayList<>();
        for (AccountInfoListDTO accountInfoListDto : accountInfoListDTOS) {
            AccountInfoListVO accountInfoListVO = new AccountInfoListVO();
            BeanUtils.copyProperties(accountInfoListDto, accountInfoListVO);
            accountInfoListVO.setRegisterTime(DateUtil.format(accountInfoListDto.getRegisterTime(), "yyyy-MM-dd"));
            switch (accountInfoListDto.getRole()) {
                case "admin" -> accountInfoListVO.setRole("管理员");
                default -> accountInfoListVO.setRole("普通用户");
            }
            accountInfoListVOS.add(accountInfoListVO);
        }
        return accountInfoListVOS;
    }

    @Override
    public AccountInfoListVO getUserInfoById(Long uId) {
        AccountInfoListDTO accountInfoListDto = userMapper.getUserInfoById(uId);
        switch (accountInfoListDto.getRole()) {
            case "admin" -> accountInfoListDto.setRole("管理员");
            default -> accountInfoListDto.setRole("普通用户");
        }
        AccountInfoListVO accountInfoListVO = new AccountInfoListVO();
        BeanUtils.copyProperties(accountInfoListDto, accountInfoListVO);
        accountInfoListVO.setRegisterTime(DateUtil.format(accountInfoListDto.getRegisterTime(), "yyyy-MM-dd"));
        return accountInfoListVO;
    }

    @Transactional
    @Override
    public void updateAccountInfo(UpdateAccountVO updateAccountVO) {
        try {
            int row = userMapper.updateAccountInfo(updateAccountVO);
            if (row != 1) {
                throw new RuntimeException("发生错误");
            }
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void resetPasswordById(Long id) {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        String password = passwordEncoder.encode(defaultPassword);
        resetPasswordDTO.setPassword(password);
        resetPasswordDTO.setId(id);
        int row = userMapper.resetPasswordById(resetPasswordDTO);
        if (row != 1) {
            throw new RuntimeException("密码重置失败");
        }
    }

    @Override
    public void insertUser(InsertAccountDTO insertAccountDTO) {
        insertAccountDTO.setPassword(passwordEncoder.encode(defaultPassword));
        int row = userMapper.insertUser(insertAccountDTO);
        if (row != 1) {
            throw new RuntimeException("插入失败");
        }
    }

    @Override
    public List<AccountInfoListVO> getAllUserInfo() {
        try {
            List<AccountInfoListDTO> accountInfoList;
            accountInfoList = userMapper.getAccountInfoListAll();
            List<AccountInfoListVO> accountInfoListVOS = new ArrayList<>();
            for (AccountInfoListDTO accountInfoListDto : accountInfoList) {
                AccountInfoListVO accountInfoListVO = new AccountInfoListVO();
                BeanUtils.copyProperties(accountInfoListDto, accountInfoListVO);
                accountInfoListVO.setRegisterTime(DateUtil.format(accountInfoListDto.getRegisterTime(), "yyyy-MM-dd"));
                switch (accountInfoListDto.getRole()) {
                    case "admin" -> accountInfoListVO.setRole("管理员");
                    default -> accountInfoListVO.setRole("普通用户");
                }
                accountInfoListVOS.add(accountInfoListVO);
            }
            return accountInfoListVOS;
        } catch (Exception e) {
            throw new RuntimeException("发生异常，请联系管理员");
        }
    }


    @Transactional
    @Override
    public void batchSaveUser(List<SaveExcelUserInfoVO> maps) {
        List<SaveExcelUserInfoDTO> saveExcelUserInfoDTOS = new ArrayList<>();

        // 遍历原始列表并拷贝每个对象的属性
        for (SaveExcelUserInfoVO vo : maps) {
            SaveExcelUserInfoDTO dto = new SaveExcelUserInfoDTO();
            BeanUtils.copyProperties(vo, dto);
            dto.setId(snowflake.nextId());
            dto.setPassword(passwordEncoder.encode(defaultPassword));
            saveExcelUserInfoDTOS.add(dto);
        }

        try {
            // 批量保存到数据库
            int row = userMapper.batchSaveUser(saveExcelUserInfoDTOS);
            if (row != saveExcelUserInfoDTOS.size()) {
                throw new RuntimeException("数据导入失败，预期插入" + saveExcelUserInfoDTOS.size() + "条，但实际插入" + row + "条");
            }
        } catch (Exception e) {
            throw new RuntimeException("批量插入失败：" + e.getMessage(), e);
        }
    }


}
