package xyz.ontip.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResetPasswordDTO implements Serializable {
    private Long id;
    private String password;
}
