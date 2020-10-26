package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

@Data
public class ErrorTextQueryCriteria {

    private Integer id;

    // 错误信息
    @Query(type = Query.Type.INNER_LIKE)
    private String labelError;

    // 标签号码
    @Query(type = Query.Type.INNER_LIKE)
    private String labelNumber;

    // 状态
    @Query(type = Query.Type.INNER_LIKE)
    private Integer labelStatus;

    // 状态
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp createDate;

}
