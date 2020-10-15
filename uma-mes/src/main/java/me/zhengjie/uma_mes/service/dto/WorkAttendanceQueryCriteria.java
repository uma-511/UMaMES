package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import me.zhengjie.annotation.Query;

/**
* @author wave
* @date 2020-09-07
*/
@Data
public class WorkAttendanceQueryCriteria{

    @Query(type = Query.Type.INNER_LIKE)
    private String personName;

    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp attenceDate;

    @Query(type = Query.Type.INNER_LIKE)
    private String attenceType;

    @Query(type = Query.Type.INNER_LIKE)
    private Integer day;

    @Query(type = Query.Type.INNER_LIKE)
    private String remark;

    private Boolean showUnEnable;

    private Long monthTime;


    /**
     * 状态
     */
    @Query(propName = "enable")
    private Boolean enable;



    /**
     * 大于等于
     */
    @Query(type = Query.Type.GREATER_THAN, propName = "attenceDate")
    private Date startTime;

    /**
     * 小于等于
     */
    @Query(type = Query.Type.LESS_THAN, propName = "attenceDate")
    private Date endTime; /**
     * 大于等于
     */
    @Query(type = Query.Type.GREATER_THAN, propName = "attenceDate")
    private String startDate;

    /**
     * 小于等于
     */
    @Query(type = Query.Type.LESS_THAN, propName = "attenceDate")
    private String endDate;

    /**
     * 是否可用
     */
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}