package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.data.annotation.CreatedDate;

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
    @Column(name = "account_code",nullable = false)
    private String accountCode;

    // 创建日期
    @CreatedDate
    @Column(name = "create_date",nullable = false)
    private Timestamp createDate;

    // 创建人
    @Column(name = "create_user",nullable = false)
    private String createUser;

    // 客户ID
    @Column(name = "customer_id",nullable = false)
    private Integer customerId;

    // 客户名称
    @Column(name = "customer_name",nullable = false)
    private String customerName;

    // 客户联系人
    @Column(name = "contacts",nullable = false)
    private String contacts;

    // 客户联系电话
    @Column(name = "contact_phone",nullable = false)
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

    // 传真
    @Column(name = "fax")
    private String fax;

    public void copy(UmaChemicalFiberStatement source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
