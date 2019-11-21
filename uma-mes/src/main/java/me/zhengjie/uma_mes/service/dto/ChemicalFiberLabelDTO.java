package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-21
*/
@Data
public class ChemicalFiberLabelDTO implements Serializable {

    private Integer id;

    // 标签编号（条码号）
    private String labelNumber;

    // 生产单id
    private Integer productionId;

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
}