package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-6-4 14:49:34
*/
@Data
@NoArgsConstructor
public class BonusJobQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

    @Query(propName = "id", joinName = "dept")
    private Long deptId;

    @Query(type = Query.Type.GREATER_THAN,propName = "createTime")
    private Timestamp startTime;

    @Query(type = Query.Type.LESS_THAN,propName = "createTime")
    private Timestamp endTime;
}