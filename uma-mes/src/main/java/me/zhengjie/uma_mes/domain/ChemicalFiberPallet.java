package me.zhengjie.uma_mes.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="uma_chemical_fiber_pallet")
public class ChemicalFiberPallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 托板号
    @Column(name = "pallet_number")
    private String palletNumber;

    // 总净重
    @Column(name = "net_weight")
    private BigDecimal netWeight;

    // 总皮重
    @Column(name = "tare")
    private BigDecimal tare;

    // 总毛重
    @Column(name = "gross_weight")
    private BigDecimal grossWeight;

    // 总个数
    @Column(name = "total_number")
    private Integer totalNumber;

    // 总件数
    @Column(name = "total_bag")
    private Integer totalBag;

    // 打印时间
    @Column(name = "print_time")
    private Timestamp printTime;

    // 打印时间
    @Column(name = "print_end_time")
    private Timestamp printEndTime;

    // 打印状态
    @Column(name = "print_status")
    private Integer printStatus;

    // 打印次数
    @Column(name = "print_number")
    private Integer printNumber;

    // 纤度
    @Column(name = "prod_fineness")
    private String prodFineness;

    // 色号
    @Column(name = "prod_color")
    private String prodColor;

    // 纤度
    @Column(name = "prod_model")
    private String prodModel;

    // 色号
    @Column(name = "prod_name")
    private String prodName;

    // 包装员
    @Column(name = "packer")
    private String packer;

}
