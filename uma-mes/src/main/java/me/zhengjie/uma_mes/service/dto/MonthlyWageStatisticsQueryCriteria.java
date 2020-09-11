package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

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

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String job;
}