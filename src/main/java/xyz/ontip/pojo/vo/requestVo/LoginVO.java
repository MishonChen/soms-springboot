package xyz.ontip.pojo.vo.requestVo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVO implements Serializable {
    private String username;
    private String password;
    private Boolean rememberMe;
}
