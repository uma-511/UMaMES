package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.util.List;

/**
* @author wave
* @date 2020-09-02
*/
@Data
public class TravelExpensesQueryCriteria{

    /**
     * 始点精准
     */
    @Query(propName = "startPlace")
    private String startPlaceAccurate;

    /**
     * 终点精准
     */
    @Query(propName = "endPlace")
    private String endPlaceAccurate;

    /**
     * 始点模糊
     */
    @Query(propName = "startPlace",type = Query.Type.INNER_LIKE)
    private String startPlace;

    /**
     * 终点模糊
     */
    @Query(propName = "endPlace",type = Query.Type.INNER_LIKE)
    private String endPlace;

    /**
     * 终点
     */
    @Query(propName = "enable")
    private Boolean enable;

    private Boolean showUnEnable;

    /**
     * 是否可用
     */
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}