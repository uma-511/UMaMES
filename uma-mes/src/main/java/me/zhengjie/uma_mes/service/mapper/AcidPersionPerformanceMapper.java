package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.AcidPersionPerformance;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-05
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcidPersionPerformanceMapper extends BaseMapper<AcidPersionPerformanceDTO, AcidPersionPerformance> {

}