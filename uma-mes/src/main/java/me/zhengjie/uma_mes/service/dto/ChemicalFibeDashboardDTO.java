package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChemicalFibeDashboardDTO implements Serializable {


    // 入库单数
    private Integer warehousingNumber;

    // 入库吨数
    private Integer warehousingTonNumber;

    // 入库支数
    private Integer warehousingBranchNumber;

    // 送货单数
    private Integer deliveryNumber;

    // 送货吨数
    private Integer deliveryTonNumber;

    // 送货支数
    private Integer deliveryBranchNumber;

    // 送货金额
    private Integer deliveryTotalNumber;

    // 库存吨数
    private Integer stockTonNumber;

    // 库存支数
    private Integer stockBranchNumber;


}
