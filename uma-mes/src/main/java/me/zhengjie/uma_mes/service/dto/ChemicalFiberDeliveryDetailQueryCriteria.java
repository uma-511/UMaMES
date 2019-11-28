package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberDeliveryDetailQueryCriteria{

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

    @Query
    private String scanNumber;
}