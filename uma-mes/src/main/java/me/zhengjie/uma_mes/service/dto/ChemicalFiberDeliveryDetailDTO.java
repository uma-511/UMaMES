package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

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

    private Integer deliveryNoteId;

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
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal cost;

    // 销售单价
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal sellingPrice;

    // 单位
    private String unit;

    // 总成本
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal totalCost;

    // 总金额
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal totalPrice;

    // 总件数
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal totalBag;

    // 总数量
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal totalNumber;

    // 总重量
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
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