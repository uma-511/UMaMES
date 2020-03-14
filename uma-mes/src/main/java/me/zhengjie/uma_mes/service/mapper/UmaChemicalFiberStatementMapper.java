package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UmaChemicalFiberStatementMapper extends BaseMapper<UmaChemicalFiberStatementDTO, UmaChemicalFiberStatement> {

}