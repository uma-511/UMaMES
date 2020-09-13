package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-02
*/
@Data
public class TravelPersionPerformanceDTO implements Serializable {

    private Integer id;

    // 责任人
    private String personName;

    // 人员id
    private Integer personId;

    /**
     * 流水号
     */
    private String scanNumber;

    // 职位
    private String permission;

    // 里程费
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal mileageFee;

    // 加班费
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal overtimePay;

    // 补贴费
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal allowance;

    // 附加费
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal surcharge;

    // 装卸费
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal handlingCost;

    // 绩效总计
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal totalPerformance;

    // 日期
    private Timestamp createTime;

    /**
     * 客户名称
     */
    private String customerName;

    // 是否可用
    private Boolean enable;
}