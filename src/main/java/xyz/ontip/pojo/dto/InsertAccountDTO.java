package xyz.ontip.pojo.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class InsertAccountDTO implements Serializable {
    private Long id;
    private String username;
    private String role;
    private String registerTime;
    private String password;
    private String nickname;
    private String address;
    private Long recommendId;
}
