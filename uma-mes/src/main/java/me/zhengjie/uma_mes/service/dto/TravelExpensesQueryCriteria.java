package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author wave
* @date 2020-09-02
*/
@Data
public class TravelExpensesQueryCriteria{

    // 始点
    @Query(propName = "startPlace")
    private String startPlace;

    // 终点
    @Query(propName = "endPlace")
    private String endPlace;
}