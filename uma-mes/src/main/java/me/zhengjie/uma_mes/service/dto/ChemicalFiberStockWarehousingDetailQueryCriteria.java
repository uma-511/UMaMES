package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class ChemicalFiberStockWarehousingDetailQueryCriteria {

    // 入库单编号
    @Query
    private String scanNumber;

    // 入库单id
    @Query
    private Integer warehousingId;


}
