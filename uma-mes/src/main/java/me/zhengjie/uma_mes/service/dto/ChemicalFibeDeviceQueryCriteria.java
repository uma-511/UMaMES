package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
@Data
public class ChemicalFibeDeviceQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String ip;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    // 精确
    @Query
    private Integer type;

    // 精确
    @Query
    private Integer status;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String groupName;

    // 模糊
    @Query
    private Integer groupId;
}