package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
* @author wave
* @date 2020-09-11
*/
@Data
public class MonthlyWageStatisticsQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String personName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String dept;

    // 精确
    @Query(type = Query.Type.EQUAL)
    private String status;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String job;



    /**
     * 大于等于
     */
    @Query(type = Query.Type.GREATER_THAN, propName = "dateTime")
    private Timestamp startTime;

    /**
     * 小于等于
     */
    @Query(type = Query.Type.LESS_THAN, propName = "dateTime")
    private Timestamp endTime;
}