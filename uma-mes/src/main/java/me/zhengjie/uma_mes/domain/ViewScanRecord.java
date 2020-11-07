package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author Xie Ji Biao
* @date 2019-11-26
*/
@Entity
@Data
@Table(name="view_scan_record")
@IdClass(ViewScanRecordPK.class)
public class ViewScanRecord implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 扫描时间
    @Id
    @Column(name = "scan_time",nullable = false)
    private Timestamp scanTime;

    // 扫描单号
    @Column(name = "scan_number",nullable = false)
    private String scanNumber;

    // 扫描类型（入库：RK 出库：SH 退库：TK 退货：TH）
    @Column(name = "type")
    private String type;

    // 标签编号（条码号）
    @Column(name = "label_number",nullable = false)
    private String labelNumber;

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

    // 订单编号
    @Column(name = "number",nullable = false)
    private String number;

    // 客户编号
    @Column(name = "customer_code")
    private String customerCode;

    // 产品名称
    @Column(name = "prod_name")
    private String prodName;

    // 产品颜色
    @Column(name = "prod_color")
    private String prodColor;

    // 产品纤度
    @Column(name = "prod_fineness")
    private String prodFineness;

    // 产品纤度
    @Column(name = "flowing_water")
    private Integer flowingWater;

    public void copy(ViewScanRecord source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
