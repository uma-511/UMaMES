package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
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

    /**
     * 是否可用
     */
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}