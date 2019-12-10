package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.HeartBeat;
import me.zhengjie.uma_mes.service.dto.HeartBeatDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2019-12-04
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HeartBeatMapper extends BaseMapper<HeartBeatDTO, HeartBeat> {

}