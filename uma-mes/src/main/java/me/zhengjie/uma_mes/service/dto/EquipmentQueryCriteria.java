package me.zhengjie.uma_mes.service.dto;


import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class EquipmentQueryCriteria {


    @Query(type = Query.Type.INNER_LIKE)
    private Integer id;

    // ip
    @Query(type = Query.Type.INNER_LIKE)
    private String ip;

    // 状态
    @Query(type = Query.Type.INNER_LIKE)
    private Integer status;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;
}
