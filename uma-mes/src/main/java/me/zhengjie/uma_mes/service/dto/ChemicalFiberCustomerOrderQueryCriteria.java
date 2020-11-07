package me.zhengjie.uma_mes.service.dto;


import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
@Data
public class ChemicalFiberCustomerOrderQueryCriteria{

    @Query
    private Integer delFlag;

}
