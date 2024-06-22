package xyz.ontip.pojo.dto.bill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UpdateBillDTO implements Serializable {
    private Long id;
    private String billCode;
    private String productName;
    private String productDesc;
    private String productUnit;
    private Integer productCount;
    private BigDecimal totalPrice;
    private Integer isPayment;
    private Long modifyBy;
    private String modifyDate;
    private Long providerId;
}
