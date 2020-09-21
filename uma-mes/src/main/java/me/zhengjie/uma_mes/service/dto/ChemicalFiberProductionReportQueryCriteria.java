package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

@Data
public class ChemicalFiberProductionReportQueryCriteria {


    // 班次
    @Query
    private String shifts;

    // 纤度
    @Query(type = Query.Type.INNER_LIKE)
    private String fineness;

    // 色号
    @Query(type = Query.Type.INNER_LIKE)
    private String color;


    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "time")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "time")
    private Timestamp endTime;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;

}
