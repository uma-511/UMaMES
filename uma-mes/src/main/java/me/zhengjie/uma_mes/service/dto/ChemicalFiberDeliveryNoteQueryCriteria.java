package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberDeliveryNoteQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String scanNumber;

    // 客户id
    @Query
    private Integer customerId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerCode;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerAddress;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String contacts;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String contactPhone;

    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "createDate")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "createDate")
    private Timestamp endTime;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;

    private Integer id;
}