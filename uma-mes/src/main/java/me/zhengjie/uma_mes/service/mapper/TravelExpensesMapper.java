package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.TravelExpenses;
import me.zhengjie.uma_mes.service.dto.TravelExpensesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-02
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TravelExpensesMapper extends BaseMapper<TravelExpensesDTO, TravelExpenses> {

}