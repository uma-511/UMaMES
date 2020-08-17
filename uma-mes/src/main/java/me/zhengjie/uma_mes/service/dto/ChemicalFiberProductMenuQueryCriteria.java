package me.zhengjie.uma_mes.service.dto;

import me.zhengjie.annotation.Query;


public class ChemicalFiberProductMenuQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private Integer id;

    @Query(type = Query.Type.INNER_LIKE)
    private String productMenusName;
}
