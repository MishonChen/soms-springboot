package xyz.ontip.pojo.dto.account;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePasswordDTO implements Serializable {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
