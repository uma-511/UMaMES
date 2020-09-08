package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletQueryCeiteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ChemicalFiberPalletService {


    Map<String,Object> queryAll(ChemicalFiberPalletQueryCeiteria criteria, Pageable pageable);

}
