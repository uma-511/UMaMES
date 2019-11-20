package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaProductChemicalFiberQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String model;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String color;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String fineness;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp createDate;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String createUser;
}