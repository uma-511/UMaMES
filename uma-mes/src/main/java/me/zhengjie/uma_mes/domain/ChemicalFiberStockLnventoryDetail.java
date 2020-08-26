package me.zhengjie.uma_mes.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "uma_chemical_fiber_stock_lnventory_detail")
public class ChemicalFiberStockLnventoryDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 盘点单id
    @Column(name = "lnventory_id")
    private Integer lnventoryId;

    // 盘点单id
    @Column(name = "stock_id")
    private Integer stockId;

    // 产品型号
    @Column(name = "prod_model")
    private String prodModel;

    // 产品名称
    @Column(name = "prod_name")
    private String prodName;

    // 单位
    @Column(name = "unit")
    private String unit;

    // 产品数量
    @Column(name = "prod_number")
    private BigDecimal prodNumber;

    // 盘点数量
    @Column(name = "lnventory_number")
    private BigDecimal lnventoryNumber;

    // 盘盈数量
    @Column(name = "lnventory_surplus")
    private BigDecimal lnventorySurplus;

    // 盘亏数量
    @Column(name = "lnventory_loss")
    private BigDecimal lnventoryLoss;

    // 盘盈数量
    @Column(name = "lnventory_surplus_str")
    private String lnventorySurplusStr;

    // 盘亏数量
    @Column(name = "lnventory_loss_str")
    private String lnventoryLossStr;


}
