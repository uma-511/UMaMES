package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author wave
* @date 2020-09-09
*/
@Data
public class BonusTypeQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String type;
}