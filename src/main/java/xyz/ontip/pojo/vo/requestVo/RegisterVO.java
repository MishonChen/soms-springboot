package xyz.ontip.pojo.vo.requestVo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterVO implements Serializable {
    private String username;
    private String password;
    private String recommendCode;
}
