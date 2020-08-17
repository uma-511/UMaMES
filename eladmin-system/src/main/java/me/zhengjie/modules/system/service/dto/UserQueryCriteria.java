package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
public class UserQueryCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;


    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private List<Long> deptIdList;

    // 多字段模糊
    @Query(blurry = "email,realname")
    private String blurry;

    @Query(propName = "username", type = Query.Type.IN)
    private String username;

    @Query(propName = "realname", type = Query.Type.IN)
    private String realname;

    @Query
    private Boolean enabled;

    @Query
    private Long deptId;

    @Query(type = Query.Type.GREATER_THAN,propName = "createTime")
    private Timestamp startTime;

    @Query(type = Query.Type.LESS_THAN,propName = "createTime")
    private Timestamp endTime;

    @Query(type = Query.Type.NOT_EQUAL, propName = "username")
    private String username_NOT_EQUAL;
}
