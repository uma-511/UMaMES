package me.zhengjie.uma_mes.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

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
@Table(name="uma_receipt")
public class Receipt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 流水号
    @Column(name = "receipt_number")
    private String receiptNumber;

    // 客户名称
    @Column(name = "customer_name")
    private String customerName;

    // 客户编号
    @Column(name = "customer_code")
    private String customerCode;

    // 客户ID
    @Column(name = "customer_id")
    private Integer customerId;

    // 收入类型
    @Column(name = "type")
    private String type;

    // 收款账号
    @Column(name = "recived_account")
    private String recivedAccount;

    // 单据日期
    @Column(name = "recived_date")
    private Timestamp recivedDate;

    // 项目类型
    @Column(name = "project_type")
    private String projectType;

    // 经办人
    @Column(name = "operator")
    private String operator;

    // 金额
    @Column(name = "amount_of_money")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal amountOfMoney;

    // 单据编号
    @Column(name = "recived_number")
    private String recivedNumber;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 创单人
    @Column(name = "create_user")
    private String createUser;

    // 创单日期
    @Column(name = "create_date")
    private Timestamp createDate;

    // 状态：0-失效 1-编辑 2-完结
    @Column(name = "status")
    private Integer status;

    // 是否可用
    @Column(name = "enable")
    private Boolean enable;

    public void copy(Receipt source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}