package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberPalletDTO  implements Serializable {

    private Integer id;

    // 托板号
    private String palletNumber;

    // 总净重
    private BigDecimal netWeight;

    // 总皮重
    private BigDecimal tare;

    // 总毛重
    private BigDecimal grossWeight;

    // 总个数
    private Integer totalNumber;

    // 总件数
    private Integer totalBag;

    // 打印时间
    private Timestamp printTime;

    // 打印时间
    private Timestamp printEndTime;

    // 打印状态
    private Integer printStatus;

    // 打印次数
    private Integer printNumber;

    // 纤度
    private String prodFineness;

    // 色号
    private String prodColor;

    // 料号
    private String prodModel;

    // 名称
    private String prodName;

    // 包装员
    private String packer;

    // 新增时间
    private Timestamp createDate;

    // 出库个数
    private Integer warehousingNumber;

    // 出库包数
    private Integer warehousingBag;

}
