package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
@Data
public class UmaChemicalFiberStatementQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String accountCode;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String contacts;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String contactPhone;
}