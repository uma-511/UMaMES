package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ChemicalFiberStockWarehousingService {

    Map<String,Object> queryAll(ChemicalFiberStockWarehousingQueryCriteria criteria, Pageable pageable);

    ChemicalFiberStockWarehousingDTO create(ChemicalFiberStockWarehousing resources);

    void update(ChemicalFiberStockWarehousing resources);

    void warehousing(List<ChemicalFiberStockWarehousingDetail> resources);

    void delete(Integer id);
}
