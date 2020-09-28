package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberSalesDTO implements Serializable {


    // 送货单号
    private String scanNumber;

    // 客户名称
    private String customerName;

    // 送货日期
    private Timestamp deliveryDate;

    // 产品名称
    private String prodName;

    // 产品单位
    private String unit;

    // 计划数量
    private BigDecimal totalNumber;

    // 实际数量
    private BigDecimal realQuantity;

    // 数量差
    private BigDecimal sumNumber;

    // 计划金额
    private BigDecimal totalPrice;

    // 实际金额
    private BigDecimal realPrice;

    // 金额差
    private BigDecimal sumPrice;






}
