package me.zhengjie.uma_mes.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author wave
* @date 2020-09-08
*/
@Entity
@Data
@Table(name="uma_attence_type")
public class AttenceType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 类型
     */
    @Column(name = "attence_type")
    private String attenceType;

    /**
     * 状态
     */
    @Column(name = "enable")
    private Boolean enable;

    /**
     * 金额
     */
    @Column(name = "price")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal price;

    public void copy(AttenceType source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}