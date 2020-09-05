package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChemicalFiberPalletMapper extends BaseMapper<ChemicalFiberPalletDTO, ChemicalFiberPallet> {
}
