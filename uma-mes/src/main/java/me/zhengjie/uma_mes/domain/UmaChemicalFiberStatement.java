package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.Query;

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
@Table(name="uma_chemical_fiber_statement")
public class UmaChemicalFiberStatement implements Serializable {


    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 对账单号
    @Column(name = "account_code")
    private String accountCode;

    // 创建日期
    @CreatedDate
    @Column(name = "create_date")
    private Timestamp createDate;

    // 创建人
    @Column(name = "create_user")
    private String createUser;

    // 客户ID
    @Column(name = "customer_id")
    private Integer customerId;

    // 客户名称
    @Column(name = "customer_name")
    private String customerName;

    // 客户联系人
    @Column(name = "contacts")
    private String contacts;

    // 客户联系电话
    @Column(name = "contact_phone")
    private String contactPhone;

    // 应收金额
    @Column(name = "receivable")
    private BigDecimal receivable;

    // 上期欠款
    @Column(name = "accumulated_arrears")
    private BigDecimal accumulatedArrears;

    // 总欠金额
    @Column(name = "total_arrears")
    private BigDecimal totalArrears;

    // 总欠金额
    @Column(name = "up_date")
    private Timestamp upDate;

    // 总欠金额
    @Column(name = "print_date")
    private Timestamp printDate;

    public void copy(UmaChemicalFiberStatement source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }



}