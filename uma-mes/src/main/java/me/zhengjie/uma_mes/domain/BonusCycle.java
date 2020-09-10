package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author wave
* @date 2020-09-10
*/
@Entity
@Data
@Table(name="uma_bonus_cycle")
public class BonusCycle implements Serializable {

    /**
     * 奖金类型id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bonus_type_id")
    private Integer bonusTypeId;

    /**
     * 周期id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bonus_cycle")
    private Integer bonusCycle;

    public void copy(BonusCycle source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}