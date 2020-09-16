package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ChemicalFiberWarehousingReortQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String scanNumber;

    @Query
    private String prodName;

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

    private Boolean queryWithInvalid;

    @Query(type = Query.Type.IN,propName = "invalid")
    private List<Integer> invalidList;

    @Query(type = Query.Type.IN,propName = "warehousingId")
    private List<Integer> warehousingIdList;

    private String createUser;

    private String supplierName;

}
