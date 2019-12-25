package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChemicalFiberProductionReportDTO implements Serializable {

    // 订单编号
    private String number;

    // 产品颜色
    private String prodColor;

    // 产品纤度
    private String prodFineness;

    // ---------------------------

    // 入库
    // 包数
    private Integer warehousingPacketNumber = 0;
    // 实际每袋个数
    private Integer warehousingFactPerBagNumber = 0;

    // 净重
    private BigDecimal warehousingNetWeight = new BigDecimal(0.0);

    // 毛重
    private BigDecimal warehousingGrossWeight = new BigDecimal(0.0);

    // 在库包数比例
    private Double warehousingPacketRatio = 0.0;

    // 净重比例
    private BigDecimal warehousingNetWeightRatio = new BigDecimal(0.0);

    // 出库
    // 包数
    private Integer outOfstockPacketNumber = 0;

    // 实际每袋个数
    private Integer outOfstockFactPerBagNumber = 0;

    // 净重
    private BigDecimal outOfstockNetWeight = new BigDecimal(0.0);

    // 毛重
    private BigDecimal outOfstockGrossWeight = new BigDecimal(0.0);

    // 作废
    // 包数
    private Integer toVoidPacketNumber = 0;

    // 实际每袋个数
    private Integer toVoidFactPerBagNumber = 0;

    // 净重
    private BigDecimal toVoidNetWeight = new BigDecimal(0.0);

    // 毛重
    private BigDecimal toVoidGrossWeight = new BigDecimal(0.0);
}
