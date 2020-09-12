package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletQueryCeiteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ChemicalFiberPalletService {


    Map<String,Object> queryAll(ChemicalFiberPalletQueryCeiteria criteria, Pageable pageable);


    void exportPoundExcel(ChemicalFiberPallet chemicalFiberPallet, HttpServletResponse response);

    List<ChemicalFiberPallet> getPallet(String palletNumber);
}
