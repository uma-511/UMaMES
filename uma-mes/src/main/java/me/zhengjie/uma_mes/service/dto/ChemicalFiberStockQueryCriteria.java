package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberStockQueryCriteria{

    // 产品id
    @Query
    private Integer id;

    // 产品id
    @Query
    private Integer prodId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodModel;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodName;

    @Query
    private String prodUnit;

    // 精确
    @Query
    private String prodColor;

    // 精确
    @Query
    private String prodFineness;

    // 总件数
    @Query(type = Query.Type.GREATER_THAN)
    private Integer totalBag;

    // 总个数
    @Query(type = Query.Type.NOT_EQUAL, propName = "totalNumber")
    private Integer number;

//    @Query(type = Query.Type.NOT_EQUAL)
//    private String max;
//
//    @Query(type = Query.Type.NOT_EQUAL)
//    private String min;
//
//    @Query(type = Query.Type.NOT_EQUAL, propName = "max")
//    private String tempMax;
//
//    @Query(type = Query.Type.NOT_EQUAL, propName = "min")
//    private String tempMin;

}