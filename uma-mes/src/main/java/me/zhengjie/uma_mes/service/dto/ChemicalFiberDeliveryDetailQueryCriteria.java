package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.util.List;

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

    @Query(type = Query.Type.IN, propName = "scanNumber")
    private List<String> scanNumbers;
}