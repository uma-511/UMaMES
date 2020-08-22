package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Data
public class ReceiptQueryCriteria{

    // 流水号
    @Query(type = Query.Type.INNER_LIKE)
    private String receiptNumber;

    // 客户名称
    @Query(type = Query.Type.INNER_LIKE)
    private String customerName;

    // 客户编号
    @Query(type = Query.Type.INNER_LIKE)
    private String customerCode;

    // 收入类型
    @Query(type = Query.Type.INNER_LIKE)
    private String type;

    // 收款方式
    @Query(type = Query.Type.INNER_LIKE)
    private String recivedAccount;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;

    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "createDate")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "createDate")
    private Timestamp endTime;

    private Boolean showUnEnable;
    // 是否可用
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}