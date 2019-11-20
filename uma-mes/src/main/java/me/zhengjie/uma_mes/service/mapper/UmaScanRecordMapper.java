package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.UmaScanRecord;
import me.zhengjie.uma_mes.service.dto.UmaScanRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UmaScanRecordMapper extends BaseMapper<UmaScanRecordDTO, UmaScanRecord> {

}