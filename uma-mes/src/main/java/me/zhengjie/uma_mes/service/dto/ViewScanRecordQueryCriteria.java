package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
* @author Xie Ji Biao
* @date 2019-11-26
*/
@Data
public class ViewScanRecordQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String scanNumber;

    // 精确
    @Query
    private String type;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String labelNumber;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String shifts;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String packer;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String number;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerCode;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodName;



    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "scanTime")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "scanTime")
    private Timestamp endTime;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;
}