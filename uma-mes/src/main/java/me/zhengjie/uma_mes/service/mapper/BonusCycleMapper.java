package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.BonusCycle;
import me.zhengjie.uma_mes.service.dto.BonusCycleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-10
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BonusCycleMapper extends BaseMapper<BonusCycleDTO, BonusCycle> {

}