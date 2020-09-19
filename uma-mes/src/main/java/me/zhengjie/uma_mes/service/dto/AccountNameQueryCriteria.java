package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Data
public class AccountNameQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String name;
}