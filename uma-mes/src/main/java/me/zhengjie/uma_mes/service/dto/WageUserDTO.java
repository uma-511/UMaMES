package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Getter
@Setter
public class WageUserDTO implements Serializable {

    private Long id;

    private String realName;

    private String job;

    private String dept;

    private BigDecimal basicSalary;
}
