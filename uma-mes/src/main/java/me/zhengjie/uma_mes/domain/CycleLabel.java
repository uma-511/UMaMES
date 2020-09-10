package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

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
    @Column(name = "id")
    private Integer id;

    /**
     * 周期
     */
    @Column(name = "name")
    private String name;

    public void copy(CycleLabel source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}