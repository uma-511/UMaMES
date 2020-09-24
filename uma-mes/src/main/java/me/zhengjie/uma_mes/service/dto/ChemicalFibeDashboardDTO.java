package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChemicalFibeDashboardDTO implements Serializable {


    // 入库单数
    private BigDecimal warehousingNumber;

    // 入库吨数
    private BigDecimal warehousingTonNumber;

    // 入库支数
    private BigDecimal warehousingBranchNumber;

    // 送货单数
    private BigDecimal deliveryNumber;

    // 送货吨数
    private BigDecimal deliveryTonNumber;

    // 送货支数
    private BigDecimal deliveryBranchNumber;

    // 送货金额
    private BigDecimal deliveryTotalNumber;

    // 库存吨数
    private BigDecimal stockTonNumber;

    // 库存支数
    private BigDecimal stockBranchNumber;


}
