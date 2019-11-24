package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberLabelQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String labelNumber;

    // 精确
    @Query
    private Integer status;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp printTime;

    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "printTime")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "printTime")
    private Timestamp endTime;

    // 精确
    @Query
    private String shifts;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String packer;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;
}