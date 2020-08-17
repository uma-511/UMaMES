package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class ChemicalFiberStockLnventoryDetailQueryCriteria {

    @Query
    private Integer lnventoryId;
}
