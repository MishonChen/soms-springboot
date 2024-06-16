package xyz.ontip.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountInfoListDto implements Serializable {
    private Long id;
    private String username;
    private String role;
    private Date registerTime;
    private String recommendCode;
    private Long recommendId;
    private String name;
    private String address;
}
