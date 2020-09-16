package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author wave
* @date 2020-09-11
*/
@Entity
@Data
@Table(name="uma_monthly_wage_statistics")
public class MonthlyWageStatistics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 姓名
     */
    @Column(name = "person_name")
    private String personName;

    /**
     * 部门
     */
    @Column(name = "dept")
    private String dept;

    /**
     * 岗位
     */
    @Column(name = "job")
    private String job;

    /**
     * 基本工资
     */
    @Column(name = "basic_salary")
    private BigDecimal basicSalary;

    /**
     * 绩效
     */
    @Column(name = "performance")
    private BigDecimal performance;

    /**
     * 打卡奖
     */
    @Column(name = "card_prize")
    private BigDecimal cardPrize;

    /**
     * 安全奖
     */
    @Column(name = "safe_prize")
    private BigDecimal safePrize;

    /**
     * 全勤奖
     */
    @Column(name = "full_prize")
    private BigDecimal fullPrize;

    /**
     * 高温津贴
     */
    @Column(name = "high_temperature_subsidy")
    private BigDecimal highTemperatureSubsidy;

    /**
     * 加班费
     */
    @Column(name = "overtime_pay")
    private BigDecimal overtimePay;

    /**
     * 其他
     */
    @Column(name = "other_prize")
    private BigDecimal otherPrize;

    /**
     * 应发工资
     */
    @Column(name = "wages_payable")
    private BigDecimal wagesPayable;

    /**
     * 出勤天数
     */
    @Column(name = "attendance")
    private BigDecimal attendance;

    /**
     * 实际出勤天数
     */
    @Column(name = "attendance_real")
    private BigDecimal attendanceReal;

    /**
     * 请假
     */
    @Column(name = "leave_count")
    private BigDecimal leaveCount;

    /**
     * 缺卡
     */
    @Column(name = "lack_card")
    private BigDecimal lackCard;

    /**
     * 违反安全
     */
    @Column(name = "violation_of_safety")
    private BigDecimal violationOfSafety;

    /**
     * 日期
     */
    @Column(name = "date_time")
    private Timestamp dateTime;

    /**
     * 实发工资
     */
    @Column(name = "net_salary")
    private BigDecimal netSalary;

    public void copy(MonthlyWageStatistics source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}