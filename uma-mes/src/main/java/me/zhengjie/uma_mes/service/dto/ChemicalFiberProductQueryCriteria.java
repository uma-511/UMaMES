package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberProductQueryCriteria{

    // 产品名称，精准查询
    @Query(propName = "model")
    private String modelAccurate;

    // 产品名称，精准查询
    @Query(propName = "name")
    private String nameAccurate;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String model;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String color;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String fineness;

    @Query
    private Integer delFlag;

    @Query
    private Integer menusId;

    @Query
    private String menus;
}