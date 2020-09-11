package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Objects;
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
    @NotNull(groups = {Update.class})
    private Long id;

    public @interface Update {}

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
    @JoinTable(name = "uma_bonus_cycle", joinColumns = {@JoinColumn(name = "bonus_type_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "bonus_cycle_id",referencedColumnName = "id")})
    private Set<CycleLabel> cycles;

    @ManyToMany
    @JoinTable(name = "uma_bonus_job", joinColumns = {@JoinColumn(name = "bonus_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "job_id",referencedColumnName = "id")})
    private Set<BonusJob> bonusJobs;

    /**
     * 状态
     */
    @Column(name = "enable")
    private Boolean enable;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BonusType bonusType = (BonusType) o;
        return Objects.equals(id, bonusType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void copy(BonusType source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}