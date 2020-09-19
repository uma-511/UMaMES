package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-07
*/
@Data
public class WorkAttendanceDTO implements Serializable {

    private Integer id;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 人员id
     */
    private Integer personId;

    /**
     * 制单日期
     */
    private Timestamp attenceDate;

    /**
     * 类型
     */
    private String attenceType;

    /**
     * 天数
     */
    private BigDecimal day;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建日期
     */
    private Timestamp createDate;

    /**
     * 状态
     */
    private Boolean enable;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 安全类型
     */
    private String safeType;

    /**
     * 金额
     */
    private BigDecimal price;
}