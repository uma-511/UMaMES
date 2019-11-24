package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class CustomerQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String code;

    // 客户编号，精准查询
    @Query(propName = "code")
    private String codeAccurate;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String address;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String contacts;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String contactPhone;

    // 删除标识
    @Query
    private Integer delFlag;
}