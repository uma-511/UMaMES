package me.zhengjie.uma_mes.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "uma_chemical_fiber_stock_lnventory")
public class ChemicalFiberStockLnventory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 盘点单单号
    @Column(name = "lnventory_number")
    private String lnventoryNumber;

    // 盘点单名字
    @Column(name = "lnventory_name")
    private String lnventoryName;

    // 盘盈数量
    @Column(name = "lnventory_surplus")
    private BigDecimal lnventorySurplus;

    // 盘亏数量
    @Column(name = "lnventory_loss")
    private BigDecimal lnventoryLoss;

    // 时间
    @Column(name = "create_date")
    private Timestamp createDate;

    // 状态
    @Column(name = "lnventory_status")
    private Integer lnventoryStatus;

    // 状态
    @Column(name = "lnventory_user")
    private String lnventoryUser;

    // 失效
    @Column(name = "invalid")
    private Integer invalid;

    // 盘盈数量
    @Column(name = "lnventory_surplus_str")
    private String lnventorySurplusStr;

    // 盘亏数量
    @Column(name = "lnventory_loss_str")
    private String lnventoryLossStr;



}
