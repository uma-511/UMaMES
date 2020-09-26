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
* @author wave
* @date 2020-09-24
*/
@Entity
@Data
@Table(name="uma_over_arrears_pay_detail")
public class OverArrearsPayDetail implements Serializable {

    /**
     * 序号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 金额
     */
    @Column(name = "price")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal price;

    /**
     * 结款日期
     */
    @Column(name = "pay_date")
    private Timestamp payDate;

    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Timestamp createDate;

    /**
     * 操作人
     */
    @Column(name = "create_user")
    private String createUser;

    /**
     * 客户名
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 客户id
     */
    @Column(name = "customer_id")
    private Integer customerId;

    /**
     * 状态
     */
    @Column(name = "enable")
    private Boolean enable;

    public void copy(OverArrearsPayDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}