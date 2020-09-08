package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberPalletDetailDTO  implements Serializable {

    private Integer id;

    // 标签编号（条码号）
    private String labelNumber;

    // 生产单id
    private Integer productionId;

    // 产品id
    private Integer productId;

    // 便签状态 0：待入库 1：入库 2：出库 3：作废 4：退库 5：退货
    private Integer status;

    // 打印时间
    private Timestamp printTime;

    // 实际每袋个数
    private Integer factPerBagNumber;

    // 净重
    private BigDecimal netWeight;

    // 皮重
    private BigDecimal tare;

    // 毛重
    private BigDecimal grossWeight;

    // 班次
    private String shifts;

    // 包装员
    private String packer;

    // 机台号
    private String machine;

    // 流水号
    private Integer flowNumber;

    // 纤度
    private String fineness;

    // 色号
    private String color;

    private BigDecimal coreWeight;

    // 托板id
    private String palletId;

}
