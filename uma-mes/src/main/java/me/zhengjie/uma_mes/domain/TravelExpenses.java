package me.zhengjie.uma_mes.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Value;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author wave
* @date 2020-09-02
*/
@Entity
@Data
@Table(name="uma_travel_expenses")
public class TravelExpenses implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 始点
    @Column(name = "start_place")
    private String startPlace;

    // 终点
    @Column(name = "end_place")
    private String endPlace;

    // 创建者
    @Column(name = "create_user")
    private String createUser;

    // 创建时间
    @Column(name = "create_time")
    private Timestamp createTime;

    // 拖头费用
    @Column(name = "tractor_price")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal tractorPrice;

    // 厢式车费用
    @Column(name = "van_price")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal vanPrice;

    // 槽罐车费用
    @Column(name = "tank_price")
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal tankPrice;

    // 是否可用
    @Column(name = "enable")
    private Boolean enable;

    public void copy(TravelExpenses source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}