package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2019-11-26
*/
@Data
public class ViewScanRecordDTO implements Serializable {

    private Integer id;

    // 扫描时间
    private Timestamp scanTime;

    // 扫描单号
    private String scanNumber;

    // 扫描类型（入库：RK 出库：SH 退库：TK 退货：TH）
    private String type;

    // 标签编号（条码号）
    private String labelNumber;

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

    // 订单编号
    private String number;

    // 客户编号
    private String customerCode;

    // 产品名称
    private String prodName;

    // 产品颜色
    private String prodColor;

    // 产品纤度
    private String prodFineness;
}