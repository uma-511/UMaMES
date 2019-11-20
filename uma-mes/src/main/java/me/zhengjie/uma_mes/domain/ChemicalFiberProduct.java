package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_product")
public class ChemicalFiberProduct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 产品型号
    @Column(name = "model",nullable = false)
    private String model;

    // 产品名称
    @Column(name = "name")
    private String name;

    // 色号
    @Column(name = "color")
    private String color;

    // 纤度
    @Column(name = "fineness")
    private String fineness;

    // 创建日期
    @Column(name = "create_date")
    private Timestamp createDate;

    // 创建人
    @Column(name = "create_user")
    private String createUser;

    // 删除标识
    @Column(name = "del_flag")
    private Integer delFlag;

    public void copy(ChemicalFiberProduct source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}