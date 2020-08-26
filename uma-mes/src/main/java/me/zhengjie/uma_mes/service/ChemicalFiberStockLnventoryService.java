package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ChemicalFiberStockLnventoryService {

    Map<String,Object> queryAll(ChemicalFiberStockLnventoryQueryCriteria criteria, Pageable pageable);

    ChemicalFiberStockLnventoryDTO create();

    void delete(Integer id);
}
