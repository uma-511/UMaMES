package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDetailQueryCeiteria;

import java.util.List;

public interface ChemicalFiberPalletDetailService {

    List<ChemicalFiberPalletDetailDTO> queryAll(ChemicalFiberPalletDetailQueryCeiteria resources);
}
