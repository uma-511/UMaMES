package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-10
*/
@Data
public class BonusCycleDTO implements Serializable {

    /**
     * 奖金类型id
     */
    private Long bonusTypeId;

    /**
     * 周期id
     */
    private Long bonusCycleId;
}