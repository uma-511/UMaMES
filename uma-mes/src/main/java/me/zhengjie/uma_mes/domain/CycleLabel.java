package me.zhengjie.uma_mes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
* @author wave
* @date 2020-09-09
*/
@Entity
@Data
@Table(name="uma_cycle_label")
public class CycleLabel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {Update.class})
    private Long id;

    public @interface Update {}

    /**
     * 周期
     */
    @Column(name = "name")
    private String name;

   /* @ManyToMany(mappedBy = "cycles")
    @JsonIgnore
    private Set<BonusType> bonusTypes;*/

    public void copy(CycleLabel source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CycleLabel cycleLabel = (CycleLabel) o;
        return Objects.equals(id, cycleLabel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}