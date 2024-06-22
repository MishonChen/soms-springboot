package xyz.ontip.pojo.dto.my;

import lombok.Data;

import java.io.Serializable;

@Data
public class MySearchBillDTO implements Serializable {
    private Long id;
    private String keyWords;
    private Integer pageSize;
    private Integer pageSizeIndex;
}
