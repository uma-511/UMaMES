package me.zhengjie.uma_mes.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author wave
* @date 2020-09-05
*/
@Entity
@Data
@Table(name="uma_acid_persion_performance")
public class AcidPersionPerformance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 责任人
     */
    @Column(name = "person")
    private String person;

    /**
     * 人员id
     */
    @Column(name = "person_id")
    private Integer personId;

    /**
     * 任务日期
     */
    @Column(name = "task_date")
    private Timestamp taskDate;

    /**
     * 产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 桶数
     */
    @Column(name = "number")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal number;

    /**
     * 规格(公斤)
     */
    @Column(name = "specifications")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal specifications;

    /**
     * 吨数
     */
    @Column(name = "weight")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal weight;

    /**
     * 单价（元）
     */
    @Column(name = "unit_price")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @Column(name = "price")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal price;

    /**
     * 状态
     */
    @Column(name = "enable")
    private Boolean enable;

    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Timestamp createDate;

    public void copy(AcidPersionPerformance source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}