package me.zhengjie.uma_mes.service.dto;


import lombok.Data;

@Data
public class ChemicalFiberSalesListQueryCriteria {

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;

    private String customerName;

    private String prodName;

    private String scanNumber;


}
