package xyz.ontip.pojo.dto.provier;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProviderDTO implements Serializable {
    private Long id;
    private String proCode;
    private String proName;
    private String proDesc;
    private String proContact;
    private String proPhone;
    private String proAddress;
    private String proFax;
    private Long createId;
    private String creationDate;
    private String modifyDate;
    private Long modifyId;
}
