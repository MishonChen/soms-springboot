package xyz.ontip.pojo.dto.my;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyBillPageDTO implements Serializable {
    private Long id;
    private Integer pageSize;
    private Integer pageSizeIndex;
}
