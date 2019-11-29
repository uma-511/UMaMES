package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

@Data
public class ChemicalFiberDeliveryNoteExportPoundExcelDto {

    // 出库单号
    private String scanNumber;

    // 产品id
    private Integer prodId;

    // 产品名称
    private String prodName;

    // 客户名称
    private String customerName;

    private Long createDate;
}
