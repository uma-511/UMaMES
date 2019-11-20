package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaStockQueryCriteria{

    // 精确
    @Query
    private String prodModel;

    // 精确
    @Query
    private String prodName;

    // 精确
    @Query
    private String prodColor;

    // 精确
    @Query
    private String prodFineness;
}