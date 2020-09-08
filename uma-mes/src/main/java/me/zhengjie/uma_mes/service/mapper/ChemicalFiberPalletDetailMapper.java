package me.zhengjie.uma_mes.service.mapper;


import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberPalletDetailMapper  extends BaseMapper<ChemicalFiberPalletDetailDTO, ChemicalFiberPalletDetail> {
}
