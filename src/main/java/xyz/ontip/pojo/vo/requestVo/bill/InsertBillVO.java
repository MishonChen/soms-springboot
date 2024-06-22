package xyz.ontip.pojo.vo.requestVo.bill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InsertBillVO implements Serializable {
    private String productName;
    private String productDesc;
    private String productUnit;
    private Integer productCount;
    private Integer isPayment;
    private Long providerId;
    private BigDecimal totalPrice;
}
