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
* @date 2020-08-14
*/
@Entity
@Data
@Table(name="uma_revenue")
public class Revenue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 收款分类
    @Column(name = "recived_type")
    private String recivedType;

    // 收款明细
    @Column(name = "recived_detail")
    private String recivedDetail;

    // 收款金额
    @Column(name = "money")
    private BigDecimal money;

    // 创建日期
    @Column(name = "create_date")
    private Timestamp createDate;

    // 收款日期
    @Column(name = "recived_date")
    private Timestamp recivedDate;

    // 操作人员
    @Column(name = "create_user")
    private String createUser;

    // 客户id
    @Column(name = "customer_id")
    private Integer customerId;

    // 客户名称
    @Column(name = "customer_name")
    private String customerName;

    public void copy(Revenue source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}