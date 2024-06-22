package xyz.ontip.pojo.vo.requestVo.provider;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateSupplierVO implements Serializable {
    private Long id;
    private String proCode;
    private String proName;
    private String proDesc;
    private String proContact;
    private String proPhone;
    private String proAddress;
    private String proFax;
}
