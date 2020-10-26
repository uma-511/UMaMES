package me.zhengjie.uma_mes.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table(name = "uma_equipment")
@Entity
public class Equipment implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 错误信息
    @Column(name = "ip")
    private String ip;

    // 标签号码
    @Column(name = "status")
    private Integer status;

    // 机台名称
    @Column(name = "name")
    private String name;

    public void copy(Equipment source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
