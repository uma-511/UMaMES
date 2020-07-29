package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Xie Ji Biao
* @date 2020-07-29
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_delivery_note_pay_detail")
public class ChemicalFiberDeliveryNotePayDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 客户id
    @Column(name = "customer_id")
    private Integer customerId;

    // 客户名称
    @Column(name = "customer_name")
    private String customerName;

    // 付款方式
    @Column(name = "payment")
    private String payment;

    // 创建日期
    @Column(name = "create_date",nullable = false)
    private Timestamp createDate;

    // 支付日期
    @Column(name = "pay_date")
    private Timestamp payDate;

    // 操作人员
    @Column(name = "input_user")
    private String inputUser;

    // 订单号
    @Column(name = "scan_number")
    private String scanNumber;

    public void copy(ChemicalFiberDeliveryNotePayDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}