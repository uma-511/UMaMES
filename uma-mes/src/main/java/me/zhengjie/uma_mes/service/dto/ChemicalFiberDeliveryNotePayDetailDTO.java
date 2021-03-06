package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-08-08
*/
@Data
public class ChemicalFiberDeliveryNotePayDetailDTO implements Serializable {

    private Integer id;

    // 客户id
    private Integer customerId;

    // 客户名称
    private String customerName;

    // 创建日期
    private Timestamp createDate;

    // 支付日期
    private Timestamp payDate;

    // 操作人员
    private String inputUser;

    // 订单号
    private String scanNumber;

    // 本次结账金额
    private BigDecimal amount;
}