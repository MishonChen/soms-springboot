package xyz.ontip.pojo.vo.requestVo.bill;

import lombok.Data;

import java.io.Serializable;

@Data
public class BillPageVO implements Serializable {
    private Integer pageSize;
    private Integer pageSizeIndex;
}
