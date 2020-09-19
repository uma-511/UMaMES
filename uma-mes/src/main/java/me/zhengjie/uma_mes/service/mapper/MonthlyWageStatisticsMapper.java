package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.MonthlyWageStatistics;
import me.zhengjie.uma_mes.service.dto.MonthlyWageStatisticsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-11
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonthlyWageStatisticsMapper extends BaseMapper<MonthlyWageStatisticsDTO, MonthlyWageStatistics> {

}