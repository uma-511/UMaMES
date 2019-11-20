package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.UmaStock;
import me.zhengjie.uma_mes.service.dto.UmaStockDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UmaStockMapper extends BaseMapper<UmaStockDTO, UmaStock> {

}