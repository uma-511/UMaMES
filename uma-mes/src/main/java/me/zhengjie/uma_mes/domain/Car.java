package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author wave
* @date 2020-08-29
*/
@Entity
@Data
@Table(name="uma_car")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 车牌号
    @Column(name = "car_number")
    private String carNumber;

    // 车辆类型
    @Column(name = "car_type")
    private String carType;

    // 负责人
    @Column(name = "car_director")
    private String carDirector;

    // 审理周期
    @Column(name = "trial_cycle")
    private String trialCycle;

    // 上次审核日期
    @Column(name = "last_trial")
    private Timestamp lastTrial;

    // 预计审核日期
    @Column(name = "expect_date")
    private Timestamp expectDate;

    // 是否可用
    @Column(name = "enable")
    private Boolean enable;

    public void copy(Car source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}