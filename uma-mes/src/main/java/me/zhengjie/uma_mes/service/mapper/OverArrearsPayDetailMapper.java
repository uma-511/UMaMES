package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.OverArrearsPayDetail;
import me.zhengjie.uma_mes.service.dto.OverArrearsPayDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wave
* @date 2020-09-24
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OverArrearsPayDetailMapper extends BaseMapper<OverArrearsPayDetailDTO, OverArrearsPayDetail> {

}