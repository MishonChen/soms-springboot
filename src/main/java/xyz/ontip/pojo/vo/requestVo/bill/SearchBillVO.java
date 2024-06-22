package xyz.ontip.pojo.vo.requestVo.bill;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBillVO implements Serializable {
    private String keyWords;
    private Integer pageSize;
    private Integer pageSizeIndex;
}
