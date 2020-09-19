package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author wave
* @date 2020-09-07
*/
@Entity
@Data
@Table(name="uma_work_attendance")
public class WorkAttendance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 人员姓名
     */
    @Column(name = "person_name")
    private String personName;

    /**
     * 人员id
     */
    @Column(name = "person_id")
    private Integer personId;

    /**
     * 制单日期
     */
    @Column(name = "attence_date")
    private Timestamp attenceDate;

    /**
     * 类型
     */
    @Column(name = "attence_type")
    private String attenceType;

    /**
     * 天数
     */
    @Column(name = "day")
    private BigDecimal day;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Timestamp createDate;

    /**
     * 状态
     */
    @Column(name = "enable")
    private Boolean enable;

    public void copy(WorkAttendance source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}