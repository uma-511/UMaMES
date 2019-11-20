package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_delivery_detail")
public class ChemicalFiberDeliveryDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 产品id
    @Column(name = "prod_id")
    private Integer prodId;

    // 产品型号
    @Column(name = "prod_model")
    private String prodModel;

    // 产品名称
    @Column(name = "prod_name")
    private String prodName;

    // 产品色号
    @Column(name = "prod_color")
    private String prodColor;

    // 产品纤度
    @Column(name = "prod_fineness")
    private String prodFineness;

    // 成本单价
    @Column(name = "cost")
    private BigDecimal cost;

    // 销售单价
    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    // 单位
    @Column(name = "unit")
    private String unit;

    // 总成本
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    // 总金额
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // 总件数
    @Column(name = "total_bag")
    private Integer totalBag;

    public void copy(ChemicalFiberDeliveryDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}