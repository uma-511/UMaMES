package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import me.zhengjie.annotation.Query;

/**
* @author wave
* @date 2020-09-24
*/
@Data
public class OverArrearsPayDetailQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp payDate;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerName;

    // 精确
    @Query
    private Boolean enable;
}