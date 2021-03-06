package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ScanRecordLabel;
import me.zhengjie.uma_mes.service.dto.ScanRecordLabelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Tan Jun Ming
* @date 2019-11-21
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScanRecordLabelMapper extends BaseMapper<ScanRecordLabelDTO, ScanRecordLabel> {

}