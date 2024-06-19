package xyz.ontip.pojo.vo.responesVo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccountInfoListVO  implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String username;
    private String role;
    private String registerTime;
    private String recommendCode;
    private Long recommendId;
    private String nickname;
    private String address;
}
