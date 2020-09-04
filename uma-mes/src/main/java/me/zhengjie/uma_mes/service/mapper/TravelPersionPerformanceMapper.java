package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.TravelPersionPerformance;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-02
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TravelPersionPerformanceMapper extends BaseMapper<TravelPersionPerformanceDTO, TravelPersionPerformance> {

}