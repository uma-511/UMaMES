package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberDeliveryNoteQueryCriteria{

    // 精确
    @Query
    private String scanNumber;

    // 精确
    @Query
    private String customerName;

    // 精确
    @Query
    private String customerCode;

    // 精确
    @Query
    private String customerAddress;

    // 精确
    @Query
    private String contacts;

    // 精确
    @Query
    private String contactPhone;
}