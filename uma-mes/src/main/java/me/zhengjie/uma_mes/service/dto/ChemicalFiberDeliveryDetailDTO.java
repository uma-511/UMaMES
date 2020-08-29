package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberDeliveryDetailDTO implements Serializable {

    private Integer id;

    // 出库单号
    private String scanNumber;

    private Integer deliveryNoteI;

    // 产品id
    private Integer prodId;

    // 产品型号
    private String prodModel;

    // 产品名称
    private String prodName;

    // 产品色号
    private String prodColor;

    // 产品纤度
    private String prodFineness;

    // 成本单价
    private BigDecimal cost;

    // 销售单价
    private BigDecimal sellingPrice;

    // 单位
    private String unit;

    // 总成本
    private BigDecimal totalCost;

    // 总金额
    private BigDecimal totalPrice;

    // 总件数
    private BigDecimal totalBag;

    // 总数量
    private BigDecimal totalNumber;

    // 总重量
    private BigDecimal totalWeight;

    // 备注
    private String remark;

    private Timestamp createDate;

    private String customerName;

    // 仓位
    private String warehouse;

    // 实收数量
    private BigDecimal realQuantity;

    // 实收金额
    private BigDecimal realPrice;

    // 产品列表编号
    private Integer detailNumber;
}