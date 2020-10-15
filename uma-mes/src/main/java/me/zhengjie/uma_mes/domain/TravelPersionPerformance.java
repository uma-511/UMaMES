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
* @date 2020-09-02
*/
@Entity
@Data
@Table(name="uma_travel_persion_performance")
public class TravelPersionPerformance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 责任人
     */
    @Column(name = "person_name")
    private String personName;

    /**
     * 流水号
     */
    @Column(name = "scan_number")
    private String scanNumber;

    /**
     * 人员id
     */
    @Column(name = "person_id")
    private Integer personId;

    /**
     * 职位
     */
    @Column(name = "permission")
    private String permission;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 发货地
     */
    @Column(name = "start_place")
    private String startPlace;

    /**
     * 目的地
     */
    @Column(name = "end_place")
    private String endPlace;

    /**
     * 里程费
     */
    @Column(name = "mileage_fee")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal mileageFee;

    /**
     * 加班费
     */
    @Column(name = "overtime_pay")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal overtimePay;

    /**
     * 补贴费
     */
    @Column(name = "allowance")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal allowance;

    /**
     * 附加费
     */
    @Column(name = "surcharge")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal surcharge;

    /**
     * 卸装费
     */
    @Column(name = "handling_cost")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal handlingCost;

    /**
     * 绩效总计
     */
    @Column(name = "total_performance")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal totalPerformance;

    /**
     * 日期
     */
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 是否可用
     */
    @Column(name = "enable")
    private Boolean enable;

    public void copy(TravelPersionPerformance source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}