package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ScanRecordQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String scanNumber;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String scanUser;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp scanTime;

    // 精确
    @Query
    private String type;
}