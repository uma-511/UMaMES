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
* @date 2020-03-11
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_statement_details")
public class UmaChemicalFiberStatementDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 对账单主键
    @Column(name = "statement_id")
    private Integer statementId;

    // 送货单号
    @Column(name = "scan_number")
    private String scanNumber;

    // 送货日期
    @Column(name = "scan_date")
    private Timestamp scanDate;

    // 产品名称
    @Column(name = "prod_name")
    private String prodName;

    // 总件数
    @Column(name = "total_bag")
    private BigDecimal totalBag;

    // 重量
    @Column(name = "net_weight")
    private BigDecimal netWeight;

    // 单价
    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    // 金额
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 预付款
    @Column(name = "advance_charge")
    private BigDecimal advanceCharge;

    // 扣除金额
    @Column(name = "amount_deducted")
    private BigDecimal amountDeducted;

    // 单位
    @Column(name = "unit")
    private String unit;

    public void copy(UmaChemicalFiberStatementDetails source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}