package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.math.BigDecimal;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class CustomerQueryCriteria{
    @Query(propName = "id")
    private Integer id;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String code;

    // 客户编号，精准查询
    @Query(propName = "code")
    private String codeAccurate;

    // 客户名称，精准查询
    @Query(propName = "name")
    private String nameAccurate;

    // 客户全称，精准查询
    @Query(propName = "fullName")
    private String fullNameAccurate;

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

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Long createDate;

    // 模糊
    @Query(type = Query.Type.LESS_THAN)
    private Long otherDate;

    private BigDecimal overArrears;

    private Boolean showAll;

    @Query(type = Query.Type.NOT_EQUAL,propName = "overArrears")
    private BigDecimal NotEqualZero;

    @Query(type = Query.Type.NOT_EQUAL,propName = "overArrears")
    private Object NotEqualNull;
}