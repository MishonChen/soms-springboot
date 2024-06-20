package xyz.ontip.pojo.vo.requestVo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InsertAccountVO implements Serializable {
    private String username;
    private String role;
    private String registerTime;
    private String nickname;
    private String address;
}
