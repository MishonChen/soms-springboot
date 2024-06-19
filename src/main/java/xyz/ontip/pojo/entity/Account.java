package xyz.ontip.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Account implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String role;
    private Date registerTime;
    private String recommendCode;
    private Long recommendId;
    private String nickname;
    private String address;
}
