package xyz.ontip.pojo.vo.requestVo.provider;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchProviderVO implements Serializable {
    private String keyWords;
    private Integer pageSize;
    private Integer pageSizeIndex;
}
