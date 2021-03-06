package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ConfigClassifyQueryCriteria{
    // 别名
    @Query
    private String alias;

    @Query
    private Integer delFlag;
}