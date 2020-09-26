package me.zhengjie.uma_mes.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 客户名称
    @Column(name = "name")
    private String name;

    // 客户编号
    @Column(name = "code")
    private String code;

    // 客户地址
    @Column(name = "address")
    private String address;

    // 联系人
    @Column(name = "contacts")
    private String contacts;

    // 联系电话
    @Column(name = "contact_phone")
    private String contactPhone;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 创建日期
    @Column(name = "create_date")
    private Timestamp createDate;

    // 客户全称
    @Column(name = "full_name")
    private String fullName;

    // 客户账号金额
    @Column(name = "account")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal account;

    // 创建人
    @Column(name = "create_user")
    private String createUser;

    // 删除标识
    @Column(name = "del_flag")
    private Integer delFlag;

    // 客户当前总欠款
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal totalArrears;

    // 客户当月总欠款
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal currentArrears;

    // 旧有欠款
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    @Column(name = "over_arrears")
    private BigDecimal overArrears;

    public void copy(Customer source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}