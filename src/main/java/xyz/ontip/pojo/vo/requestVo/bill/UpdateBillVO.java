package xyz.ontip.pojo.vo.requestVo.bill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UpdateBillVO implements Serializable {
    private Long id;
    private String billCode;
    private String productName;
    private String productDesc;
    private String productUnit;
    private Integer productCount;
    private Integer isPayment;
    private Long providerId;
    private BigDecimal totalPrice;
}
