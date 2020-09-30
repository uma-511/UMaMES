package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
@Data
public class UmaChemicalFiberStatementDetailsQueryCriteria{
    @Query
    private Integer statementId;

    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "scanDate")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "scanDate")
    private Timestamp endTime;

    @Query
    private String scanNumber;
}