package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author wave
* @date 2020-08-29
*/
@Data
public class CarDTO implements Serializable {

    private Integer id;

    // 车牌号
    private String carNumber;

    // 车辆类型
    private String carType;

    // 负责人
    private String carDirector;

    // 审理周期
    private String trialCycle;

    // 上次审核日期
    private Timestamp lastTrial;

    // 预计审核日期
    private Timestamp expectDate;

    // 是否可用
    private Boolean enable;
}