package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

import me.zhengjie.annotation.Query;

/**
* @author wave
* @date 2020-09-05
*/
@Data
public class AcidPersionPerformanceQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String person;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp taskDate;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String productName;

    private Boolean showUnEnable;

    // 是否可用
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}