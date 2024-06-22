package xyz.ontip.pojo.vo.responesVo.bill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillVO  implements Serializable {
    private Long id;
    private String billCode;
    private String productName;
    private String productDesc;
    private String productUnit;
    private Integer productCount;
    private String totalPrice;
    private String isPayment;
    private Long createdBy;
    private Date creationDate;
    private Long modifyBy;
    private Date modifyDate;
    private String proName;
}
