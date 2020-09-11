package me.zhengjie.uma_mes.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Getter
@Setter
@NoArgsConstructor
public class BonusJobDTO implements Serializable {

    private Long id;

    private Long sort;

    private String name;

    private Boolean enabled;

    private String deptSuperiorName;

    private Timestamp createTime;

    private BigDecimal basicSalary;

    public BonusJobDTO(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}