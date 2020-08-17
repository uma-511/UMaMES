package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberProductMenu;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberProductMenuMapper extends BaseMapper<ChemicalFiberProductMenuDTO, ChemicalFiberProductMenu> {

}
