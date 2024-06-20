package xyz.ontip.pojo.vo.requestVo.account;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchAccountInfo implements Serializable {
    private String keyWords;
    private Integer pageSize;
    private Integer pageSizeIndex;
}
