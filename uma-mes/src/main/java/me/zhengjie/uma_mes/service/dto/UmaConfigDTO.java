package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaConfigDTO implements Serializable {

    private Integer id;

    // 分类id
    private Integer classifyId;

    // 配置名称
    private String name;

    // 配置值
    private String value;

    // 默认值
    private Integer isDefault;
}