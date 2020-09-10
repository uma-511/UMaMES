package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author wave
* @date 2020-09-08
*/
@Data
public class AttenceTypeQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String attenceType;

    // 精确
    @Query
    private Boolean enable;
}