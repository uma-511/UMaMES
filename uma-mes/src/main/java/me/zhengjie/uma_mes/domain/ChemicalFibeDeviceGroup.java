package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
@Entity
@Data
@Table(name="uma_chemical_fibe_device_group")
public class ChemicalFibeDeviceGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 设备分组名称
    @Column(name = "name",nullable = false)
    private String name;

    // 使用状态：0：停用 1：正常
    @Column(name = "status",nullable = false)
    private Integer status;

    public void copy(ChemicalFibeDeviceGroup source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}