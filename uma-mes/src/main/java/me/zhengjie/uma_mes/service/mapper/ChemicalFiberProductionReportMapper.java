package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberProductionReport;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberProductionReportMapper extends BaseMapper<ChemicalFiberProductionReportDTO, ChemicalFiberProductionReport> {
}
