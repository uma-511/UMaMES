package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Set;

/**
* @author wave
* @date 2020-09-09
*/
@Entity
@Data
@Table(name="uma_bonus_type")
public class BonusType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 奖金类别
     */
    @Column(name = "type")
    private String type;

    /**
     * 金额
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Timestamp createTime;



    @ManyToMany
    @JoinTable(name = "uma_bonus_cycle", joinColumns = {@JoinColumn(name = "bonus_type_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "bonus_cycle",referencedColumnName = "id")})
    private Set<BonusCycle> cycleMenus;

    /**
     * 状态
     */
    @Column(name = "enable")
    private Boolean enable;

    public void copy(BonusType source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}