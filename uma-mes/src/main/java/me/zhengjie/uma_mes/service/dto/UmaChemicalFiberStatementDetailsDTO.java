package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
@Data
public class UmaChemicalFiberStatementDetailsDTO implements Serializable {

    private Integer id;

    // 对账单主键
    private Integer statementId;

    // 送货单号
    private String scanNumber;

    // 送货日期
    private Timestamp scanDate;

    // 产品名称
    private String prodName;

    // 总件数
    private BigDecimal totalBag;

    // 重量
    private BigDecimal netWeight;

    // 单价
    private BigDecimal sellingPrice;

    // 金额
    private BigDecimal totalPrice;

    // 备注
    private String remark;

    // 预付款
    private BigDecimal advanceCharge;

    // 扣除金额
    private BigDecimal amountDeducted;

    private String unit;
}