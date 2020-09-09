package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

@Data
public class ChemicalFiberPalletQueryCeiteria {

    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "printTime")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "printTime")
    private Timestamp endTime;


    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;


    // 纤度
    @Query(type = Query.Type.INNER_LIKE)
    private String prodFineness;

    // 色号
    @Query(type = Query.Type.INNER_LIKE)
    private String prodColor;


    // 托板号
    @Query(type = Query.Type.INNER_LIKE)
    private String palletNumber;

    // 包装员
    @Query(type = Query.Type.INNER_LIKE)
    private String packer;



}
