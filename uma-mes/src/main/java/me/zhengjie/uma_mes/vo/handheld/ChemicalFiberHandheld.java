package me.zhengjie.uma_mes.vo.handheld;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChemicalFiberHandheld {

    // 产品型号
    private String prodModel;

    // 产品名称
    private String prodName;

    // 产品颜色
    private String prodColor;

    // 产品纤度
    private String prodFineness;

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
