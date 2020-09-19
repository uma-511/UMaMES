package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-02
*/
@Data
public class TravelExpensesDTO implements Serializable {

    private Integer id;

    // 始点
    private String startPlace;

    // 终点
    private String endPlace;

    // 创建者
    private String createUser;

    // 创建时间
    private Timestamp createTime;

    // 拖头费用
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal tractorPrice;

    // 厢式车费用
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal vanPrice;

    // 槽罐车费用
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal tankPrice;

    // 是否可用
    private Boolean enable;
}