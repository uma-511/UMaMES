package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ViewScanRecord;
import me.zhengjie.uma_mes.service.dto.ViewScanRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2019-11-26
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ViewScanRecordMapper extends BaseMapper<ViewScanRecordDTO, ViewScanRecord> {

}