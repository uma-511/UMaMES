package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-05
*/
@Data
public class AcidPersionPerformanceDTO implements Serializable {

    private Integer id;

    // 责任人
    private String person;

    // 人员id
    private Integer personId;

    // 任务日期
    private Timestamp taskDate;

    // 产品名称
    private String productName;

    // 产品编号
    private String productCode;

    // 桶数
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal number;

    // 规格(公斤)
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal specifications;

    // 吨数
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal weight;

    // 单价（元）
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal unitPrice;

    // 金额
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal price;

    // 状态
    private Boolean enable;

    // 创建日期
    private Timestamp createDate;
}