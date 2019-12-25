package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ChemicalFiberDeliveryNoteSalesReportDTO implements Serializable {
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
    // 详情列表
    private List<ChemicalFiberDeliveryDetail> chemicalFiberDeliveryDetails;
}
