package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2020-07-29
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberDeliveryNotePayDetailMapper extends BaseMapper<ChemicalFiberDeliveryNotePayDetailDTO, ChemicalFiberDeliveryNotePayDetail> {

}