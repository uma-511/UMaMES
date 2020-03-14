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
    @Column(name = "statement_id",nullable = false)
    private Integer statementId;

    // 送货单号
    @Column(name = "scan_number",nullable = false)
    private String scanNumber;

    // 送货日期
    @Column(name = "scan_date",nullable = false)
    private Timestamp scanDate;

    // 产品名称
    @Column(name = "prod_name",nullable = false)
    private String prodName;

    // 总件数
    @Column(name = "total_bag",nullable = false)
    private Integer totalBag;

    // 重量
    @Column(name = "net_weight",nullable = false)
    private BigDecimal netWeight;

    // 单价
    @Column(name = "selling_price",nullable = false)
    private BigDecimal sellingPrice;

    // 金额
    @Column(name = "total_price",nullable = false)
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

    public void copy(UmaChemicalFiberStatementDetails source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}