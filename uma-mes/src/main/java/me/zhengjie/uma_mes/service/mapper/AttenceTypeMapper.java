package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.AttenceType;
import me.zhengjie.uma_mes.service.dto.AttenceTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-08
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttenceTypeMapper extends BaseMapper<AttenceTypeDTO, AttenceType> {

}