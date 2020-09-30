package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
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

    Result getWarehousingSummaries(ChemicalFiberStockWarehousingQueryCriteria criteria);

    void delete(Integer id);
}
