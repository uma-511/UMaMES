package me.zhengjie.uma_mes.vo.handheld;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChemicalFiberLabelInfoVo {

    // 条码号
    private String labelNumber;

    // 每袋个数
    private Integer factPerBagNumber;

    // 净重
    private BigDecimal netWeight;

    // 皮重
    private BigDecimal tare;

    // 毛重
    private BigDecimal grossWeight;
}
