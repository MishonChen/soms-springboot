package xyz.ontip.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaveExcelUserInfoDTO implements Serializable {
    private Long id;
    private String username;
    private String role;
    private String password;
    private String registerTime;
    private String recommendCode;
    private Long recommendId;
    private String nickname;
    private String address;
}
