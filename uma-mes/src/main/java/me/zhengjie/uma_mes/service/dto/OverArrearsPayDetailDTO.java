package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-24
*/
@Data
public class OverArrearsPayDetailDTO implements Serializable {

    /**
     * 序号
     */
    private Integer id;

    /**
     * 金额
     */
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal price;

    /**
     * 结款日期
     */
    private Timestamp payDate;

    /**
     * 创建日期
     */
    private Timestamp createDate;

    /**
     * 操作人
     */
    private String createUser;

    /**
     * 客户名
     */
    private String customerName;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 状态
     */
    private Boolean enable;
}