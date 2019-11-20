package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_config")
public class UmaConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 分类id
    @Column(name = "classify_id",nullable = false)
    private Integer classifyId;

    // 配置名称
    @Column(name = "name",nullable = false)
    private String name;

    // 配置值
    @Column(name = "value",nullable = false)
    private String value;

    // 默认值
    @Column(name = "is_default")
    private Integer isDefault;

    public void copy(UmaConfig source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}