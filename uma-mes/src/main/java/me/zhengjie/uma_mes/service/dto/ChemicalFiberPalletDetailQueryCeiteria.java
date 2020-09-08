package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class ChemicalFiberPalletDetailQueryCeiteria {

    // 托板id
    @Query
    private String palletId;
}
