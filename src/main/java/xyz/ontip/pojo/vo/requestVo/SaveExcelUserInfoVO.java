package xyz.ontip.pojo.vo.requestVo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaveExcelUserInfoVO implements Serializable {
    private String username;
    private String role;
    private String registerTime;
    private String recommendCode;
    private Long recommendId;
    private String nickname;
    private String address;
}
