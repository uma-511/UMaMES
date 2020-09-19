package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-11
*/
@Data
public class MonthlyWageStatisticsDTO implements Serializable {

    private Integer id;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 部门
     */
    private String dept;

    /**
     * 岗位
     */
    private String job;

    /**
     * 基本工资
     */
    private BigDecimal basicSalary;

    /**
     * 绩效
     */
    private BigDecimal performance;

    /**
     * 打卡奖
     */
    private BigDecimal cardPrize;

    /**
     * 安全奖
     */
    private BigDecimal safePrize;

    /**
     * 全勤奖
     */
    private BigDecimal fullPrize;

    /**
     * 高温津贴
     */
    private BigDecimal highTemperatureSubsidy;

    /**
     * 加班费
     */
    private BigDecimal overtimePay;

    /**
     * 其他
     */
    private BigDecimal otherPrize;

    /**
     * 应发工资
     */
    private BigDecimal wagesPayable;

    /**
     * 出勤天数
     */
    private BigDecimal attendance;

    /**
     * 实际出勤天数
     */
    private BigDecimal attendanceReal;

    /**
     * 请假
     */
    private BigDecimal leaveCount;

    /**
     * 缺卡
     */
    private BigDecimal lackCard;

    /**
     * 违反安全
     */
    private BigDecimal violationOfSafety;

    /**
     * 日期
     */
    private Timestamp dateTime;

    /**
     * 实发工资
     */
    private BigDecimal netSalary;
}