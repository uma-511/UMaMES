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
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_label")
public class ChemicalFiberLabel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 条码号
    @Column(name = "label_number",nullable = false)
    private String labelNumber;

    // 生产单id
    @Column(name = "production_id",nullable = false)
    private Integer productionId;

    // 状态
    @Column(name = "status")
    private Integer status;

    // 打印时间
    @Column(name = "print_time")
    private Timestamp printTime;

    // 每袋个数
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

    // 入库单号
    @Column(name = "rk_number")
    private String rkNumber;

    // 送货单号
    @Column(name = "sh_number")
    private String shNumber;

    // 退库单号
    @Column(name = "tk_number")
    private String tkNumber;

    // 退货单号
    @Column(name = "th_number")
    private String thNumber;

    // 入库扫描时间
    @Column(name = "rk_scan_time")
    private Timestamp rkScanTime;

    // 出库扫描时间
    @Column(name = "sh_scan_time")
    private Timestamp shScanTime;

    // 退库扫描时间
    @Column(name = "tk_scan_time")
    private Timestamp tkScanTime;

    // 退货扫描时间
    @Column(name = "th_scan_time")
    private Timestamp thScanTime;

    public void copy(ChemicalFiberLabel source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}