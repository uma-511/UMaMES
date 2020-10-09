package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;


/**
* @author wave
* @date 2020-09-08
*/
@Data
public class AttenceTypeDTO implements Serializable {

    private Integer id;

    /**
     * 类型
     */
    private String attenceType;

    /**
     * 状态
     */
    private Boolean enable;

    /**
     * 金额
     */
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal price;
}