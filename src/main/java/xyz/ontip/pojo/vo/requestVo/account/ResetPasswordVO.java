package xyz.ontip.pojo.vo.requestVo.account;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResetPasswordVO implements Serializable {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
