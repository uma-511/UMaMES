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
@Table(name="uma_chemical_fiber_stock")
@NamedStoredProcedureQuery(name = "Stock.storedProcedure", procedureName = "proc_update_stock")
public class ChemicalFiberStock implements Serializable {

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

    // 总净重
    @Column(name = "total_net_weight")
    private BigDecimal totalNetWeight;

    // 总皮重
    @Column(name = "total_tare")
    private BigDecimal totalTare;

    // 总毛重
    @Column(name = "total_gross_weight")
    private BigDecimal totalGrossWeight;

    // 总个数
    @Column(name = "total_number")
    private Integer totalNumber;

    // 总件数
    @Column(name = "total_bag")
    private Integer totalBag;

    // 最大值
    @Column(name = "max")
    private Integer max;

    // 最小值
    @Column(name = "min")
    private Integer min;

    // 库存指标
    @Column(name = "flag")
    private String flag;

    // 状态
    @Column(name = "status")
    private Integer status;

    public void copy(ChemicalFiberStock source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}