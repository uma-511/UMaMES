package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.BonusType;
import me.zhengjie.uma_mes.service.dto.BonusTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-09
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BonusTypeMapper extends BaseMapper<BonusTypeDTO, BonusType> {

}