package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaConfigClassifyDTO implements Serializable {

    private Integer id;

    // 配置分类名称
    private String name;

    // 别名
    private String alias;

    private Integer delFlag;
}