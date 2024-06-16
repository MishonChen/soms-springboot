package xyz.ontip.pojo.vo.responesVo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountInfoListVO  implements Serializable {
    private Long id;
    private String username;
    private String role;
    private String registerTime;
    private String recommendCode;
    private Long recommendId;
    private String name;
    private String address;
}
