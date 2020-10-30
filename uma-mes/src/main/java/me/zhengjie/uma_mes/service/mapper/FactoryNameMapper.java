package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.FactoryName;
import me.zhengjie.uma_mes.service.dto.FactoryNameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FactoryNameMapper extends BaseMapper<FactoryNameDTO, FactoryName> {
}
