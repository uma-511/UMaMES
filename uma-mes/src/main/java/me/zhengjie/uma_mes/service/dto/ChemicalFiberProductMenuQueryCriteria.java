package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class ChemicalFiberProductMenuQueryCriteria {

    @Query
    private Integer id;

    @Query
    private String productMenusName;
}
