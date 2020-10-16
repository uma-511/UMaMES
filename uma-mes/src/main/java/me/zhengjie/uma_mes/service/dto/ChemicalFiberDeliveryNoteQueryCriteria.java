package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;

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

    private String prodModel;

    // 大于等于
    @Query(type = Query.Type.GREATER_THAN, propName = "deliveryDate")
    private Timestamp startTime;

    // 小于等于
    @Query(type = Query.Type.LESS_THAN, propName = "deliveryDate")
    private Timestamp endTime;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;

    private Integer id;

    private Boolean queryWithInvalid;

    @Query(type = Query.Type.IN,propName = "invalid")
    private List<Integer> invalidList;

    private Boolean showUnEnable;
    // 是否可用
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}