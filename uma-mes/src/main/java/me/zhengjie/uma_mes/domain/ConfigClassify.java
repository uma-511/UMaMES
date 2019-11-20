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
@Table(name="uma_config_classify")
public class ConfigClassify implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 配置分类名称
    @Column(name = "name",nullable = false)
    private String name;

    // 别名
    @Column(name = "alias",nullable = false)
    private String alias;

    @Column(name = "del_flag")
    private Integer delFlag;

    public void copy(ConfigClassify source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}