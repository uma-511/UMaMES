package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberProductionQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String number;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodModel;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodColor;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodFineness;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerContactPhone;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerContacts;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerAddress;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerCode;
}