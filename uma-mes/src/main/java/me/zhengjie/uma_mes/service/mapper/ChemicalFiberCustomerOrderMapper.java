package me.zhengjie.uma_mes.service.mapper;


import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberCustomerOrder;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberCustomerOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberCustomerOrderMapper extends BaseMapper<ChemicalFiberCustomerOrderDTO, ChemicalFiberCustomerOrder> {

}
