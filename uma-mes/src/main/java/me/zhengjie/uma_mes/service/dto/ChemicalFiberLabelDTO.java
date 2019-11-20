package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberLabelDTO implements Serializable {

    private Integer id;

    // 条码号
    private String labelNumber;

    // 生产单id
    private Integer productionId;

    // 状态
    private Integer status;

    // 打印时间
    private Timestamp printTime;

    // 每袋个数
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

    // 入库单号
    private String rkNumber;

    // 送货单号
    private String shNumber;

    // 退库单号
    private String tkNumber;

    // 退货单号
    private String thNumber;

    // 入库扫描时间
    private Timestamp rkScanTime;

    // 出库扫描时间
    private Timestamp shScanTime;

    // 退库扫描时间
    private Timestamp tkScanTime;

    // 退货扫描时间
    private Timestamp thScanTime;
}