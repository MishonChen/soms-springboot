package xyz.ontip.pojo.dto.bill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillDTO  implements Serializable {
    private Long id;
    private String billCode;
    private String productName;
    private String productDesc;
    private String productUnit;
    private Integer productCount;
    private BigDecimal totalPrice;
    private Integer isPayment;
    private String nickname;
    private Date creationDate;
    private Long modifyBy;
    private Date modifyDate;
    private String proName;
}
