package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventoryDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberStockLnventoryDetailMapper extends BaseMapper<ChemicalFiberStockLnventoryDetailDTO, ChemicalFiberStockLnventoryDetail> {

}
