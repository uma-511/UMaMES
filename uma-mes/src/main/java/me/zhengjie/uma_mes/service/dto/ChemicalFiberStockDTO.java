package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberStockDTO implements Serializable {

    private Integer id;

    // 产品id
    private Integer prodId;

    // 产品型号
    private String prodModel;

    // 产品名称
    private String prodName;

    // 产品色号
    private String prodColor;

    // 产品纤度
    private String prodFineness;

    //计量单位
    private String prodUnit;

    // 总净重
    private BigDecimal totalNetWeight;

    // 总皮重
    private BigDecimal totalTare;

    // 总毛重
    private BigDecimal totalGrossWeight;

    // 总个数
    private Integer totalNumber;

    // 总件数
    private Integer totalBag;

    // 最大值
    private Integer max;

    // 最小值
    private Integer min;

    // 库存指标
    private String flag;

    // 状态
    private Integer status;

    //Label
    private String lable;
}