package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

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

    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "createTime")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "createTime")
    private Timestamp endTime;

    // 删除标识
    @Query
    private Integer delFlag;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;

    private String machineNumber;

    private String shifts;

    @Query
    private String customerOrderNumber;
}
