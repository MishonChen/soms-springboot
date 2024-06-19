package xyz.ontip.pojo.vo.requestVo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchUserInfo implements Serializable {
    private String keyWords;
    private Integer pageSize;
    private Integer pageSizeIndex;
}
