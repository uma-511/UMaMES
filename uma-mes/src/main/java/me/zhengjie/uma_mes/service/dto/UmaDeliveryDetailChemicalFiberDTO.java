package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaDeliveryDetailChemicalFiberDTO implements Serializable {

    private Integer id;

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
    private Integer totalBag;
}