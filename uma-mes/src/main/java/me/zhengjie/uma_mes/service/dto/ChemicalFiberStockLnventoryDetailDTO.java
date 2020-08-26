package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChemicalFiberStockLnventoryDetailDTO  implements Serializable {

    private Integer id;

    // 盘点单id
    private Integer lnventoryId;

    // 盘点单id
    private Integer stockId;

    // 产品型号
    private String prodModel;

    // 产品名称
    private String prodName;

    // 单位
    private String unit;

    // 产品数量
    private BigDecimal prodNumber;

    // 盘点数量
    private BigDecimal lnventoryNumber;

    // 盘盈数量
    private BigDecimal lnventorySurplus;

    // 盘亏数量
    private BigDecimal lnventoryLoss;


    // 盘盈数量
    private String lnventorySurplusStr;

    // 盘亏数量
    private String lnventoryLossStr;
}
