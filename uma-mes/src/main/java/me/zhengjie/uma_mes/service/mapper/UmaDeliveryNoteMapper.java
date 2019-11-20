package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.UmaDeliveryNote;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryNoteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UmaDeliveryNoteMapper extends BaseMapper<UmaDeliveryNoteDTO, UmaDeliveryNote> {

}