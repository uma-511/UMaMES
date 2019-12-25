package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChemicalFiberDeliveryDetailSalesReportDTO implements Serializable {
    // 出库单号
    private String scanNumber = "";
    // 客户名称
    private String customerName = "";
    // 色号
    private String prodColor = "";
    // 纤度
    private String prodFineness = "";
    // 成本单价
    private BigDecimal cost = new BigDecimal(0.0);
    // 总成本
    private BigDecimal totalCost = new BigDecimal(0.0);
    // 销售单价
    private BigDecimal sellingPrice = new BigDecimal(0.0);
    // 总金额
    private BigDecimal totalPrice = new BigDecimal(0.0);
    // 应收金额
    private BigDecimal receivablePrice = new BigDecimal(0.0);
    // 包数
    private Integer warehousingPacketNumber = 0;
    // 实际每袋个数
    private Integer warehousingFactPerBagNumber = 0;
    // 净重
    private BigDecimal warehousingNetWeight = new BigDecimal(0.0);
}
