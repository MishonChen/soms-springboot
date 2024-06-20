package xyz.ontip.pojo.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商信息实体
 */
@Data
public class Provider  implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String proCode;
    private String proName;
    private String proDesc;
    private String proPhone;
    private String proAddress;
    private String proFax;
    private Long createId;
    private Date creationDate;
    private Date modifyDate;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modifyId;
}
