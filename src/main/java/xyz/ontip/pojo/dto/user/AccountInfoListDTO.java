package xyz.ontip.pojo.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountInfoListDTO implements Serializable {
    private Long id;
    private String username;
    private String role;
    private Date registerTime;
    private String recommendCode;
    private Long recommendId;
    private String nickname;
    private String address;
}
