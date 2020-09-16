package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
* @author wave
* @date 2020-09-02
*/
@Data
public class TravelPersionPerformanceQueryCriteria{

    /**
     * 责任人
     */
    @Query(propName = "personName",type = Query.Type.INNER_LIKE)
    private String personName;

    /**
     * 状态
     */
    @Query(propName = "enable")
    private Boolean enable;

    private Boolean showUnEnable;

    /**
     * 大于等于
      */
    @Query(type = Query.Type.GREATER_THAN, propName = "createTime")
    private Timestamp startTime;

    /**
     * 小于等于
     */
    @Query(type = Query.Type.LESS_THAN, propName = "createTime")
    private Timestamp endTime;

    /**
     * 是否可用
     */
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}