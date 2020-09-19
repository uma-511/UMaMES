package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.WorkAttendance;
import me.zhengjie.uma_mes.service.dto.WorkAttendanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-07
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkAttendanceMapper extends BaseMapper<WorkAttendanceDTO, WorkAttendance> {

}