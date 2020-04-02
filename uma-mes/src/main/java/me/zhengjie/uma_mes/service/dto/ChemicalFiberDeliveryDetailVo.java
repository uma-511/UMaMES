package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberDeliveryDetailVo  {
    // 单号
    private String scanNumber;
    // 客户名称
    private String customerName;
    // 出库包数
    private Integer outOfStockPackageNumber = 0;
    // 出库个数
    private Integer outOfStockFactPerBagNumber = 0;
    // 出库净重
    private BigDecimal outOfStockNetWeight = new BigDecimal(0.0);
    // 应收金额
    private BigDecimal receivablePrice = new BigDecimal(0.0);
    // 总成本
    private BigDecimal totalCost = new BigDecimal(0.0);
    // 制单时间
    private Timestamp createDate;

//    ----------------------------
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
    private BigDecimal detailTotalCost;

    // 总金额
    private BigDecimal totalPrice;

    // 总件数
    private Integer totalBag;

    // 总数量
    private Integer totalNumber;

    // 总重量
    private BigDecimal totalWeight;

    // 备注
    private String remark;
}
