package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberStockWarehousingDetailMapper extends BaseMapper<ChemicalFiberStockWarehousingDetailDTO, ChemicalFiberStockWarehousingDetail> {
}
