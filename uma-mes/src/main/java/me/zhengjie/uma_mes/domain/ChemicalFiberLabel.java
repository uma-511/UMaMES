package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author Tan Jun Ming
 * @date 2019-11-21
 */
@Entity
@Data
@Table(name = "uma_chemical_fiber_label")
public class ChemicalFiberLabel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 标签编号（条码号）
    @Column(name = "label_number", nullable = false)
    private String labelNumber;

    // 生产单id
    @Column(name = "production_id", nullable = false)
    private Integer productionId;

    // 产品id
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    // 便签状态 0：待入库 1：入库 2：出库 3：作废 4：退库 5：退货
    @Column(name = "status")
    private Integer status;

    // 打印时间
    @Column(name = "print_time")
    private Timestamp printTime;

    // 实际每袋个数
    @Column(name = "fact_per_bag_number")
    private Integer factPerBagNumber;

    // 净重
    @Column(name = "net_weight")
    private BigDecimal netWeight;

    // 皮重
    @Column(name = "tare")
    private BigDecimal tare;

    // 毛重
    @Column(name = "gross_weight")
    private BigDecimal grossWeight;

    // 班次
    @Column(name = "shifts")
    private String shifts;

    // 包装员
    @Column(name = "packer")
    private String packer;

    // 机台号
    @Column(name = "machine")
    private String machine;

    // 流水号
    @Column(name = "flow_number")
    private Integer flowNumber;

    // 纤度
    @Column(name = "fineness")
    private String fineness;

    // 色号
    @Column(name = "color")
    private String color;

    public void copy(ChemicalFiberLabel source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}