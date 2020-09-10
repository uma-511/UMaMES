package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.uma_mes.domain.BonusCycle;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Set;


/**
* @author wave
* @date 2020-09-09
*/
@Data
public class BonusTypeDTO implements Serializable {

    private Integer id;

    /**
     * 奖金类别
     */
    private String type;

    /**
     * 金额
     */
    private BigDecimal price;

    private Set<BonusCycle> cycleMenus;

    /**
     * 创建日期
     */
    private Timestamp createTime;

    /**
     * 状态
     */
    private Boolean enable;
}