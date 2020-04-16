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
@Table(name="uma_chemical_fibe_device")
public class ChemicalFibeDevice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 设备ip
    @Column(name = "ip",nullable = false)
    private String ip;

    // 设备名称
    @Column(name = "name",nullable = false)
    private String name;

    // 设备类型：0：打印机 1：触摸屏 2：扫描抢 3：秤 4：标签打印机 5：手持机
    @Column(name = "type",nullable = false)
    private Integer type;

    // 使用状态：1：正常，0：停用
    @Column(name = "status",nullable = false)
    private Integer status;

    // 设备分组id
    @Column(name = "group_id")
    private Integer groupId;

    // 设备分组名称
    @Column(name = "group_name")
    private String groupName;

    public void copy(ChemicalFibeDevice source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}