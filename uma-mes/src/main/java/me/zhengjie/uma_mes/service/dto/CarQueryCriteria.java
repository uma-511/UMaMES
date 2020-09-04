package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.util.List;

/**
* @author wave
* @date 2020-08-29
*/
@Data
public class CarQueryCriteria{

    // 车牌号
    @Query(type = Query.Type.INNER_LIKE)
    private String carNumber;

    // 车辆类型
    @Query(type = Query.Type.INNER_LIKE)
    private String carType;

    // 负责人
    @Query(type = Query.Type.INNER_LIKE)
    private String carDirector;

    // 审理周期
    @Query(type = Query.Type.INNER_LIKE)
    private String trialCycle;

    private Boolean showUnEnable;

    // 是否可用
    @Query(type = Query.Type.IN,propName = "enable")
    private List<Boolean> enableList;
}