package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabelInventory;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelInventoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2020-04-14
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberLabelInventoryMapper extends BaseMapper<ChemicalFiberLabelInventoryDTO, ChemicalFiberLabelInventory> {

}