package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFibeDeviceGroup;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceGroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFibeDeviceGroupMapper extends BaseMapper<ChemicalFibeDeviceGroupDTO, ChemicalFibeDeviceGroup> {

}