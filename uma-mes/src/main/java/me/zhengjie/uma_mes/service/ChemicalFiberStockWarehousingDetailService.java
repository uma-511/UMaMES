package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailQueryCriteria;

import java.util.List;

public interface ChemicalFiberStockWarehousingDetailService {

    ChemicalFiberStockWarehousingDetailDTO create(ChemicalFiberStockWarehousingDetail resources);

    List<ChemicalFiberStockWarehousingDetailDTO> queryAll(ChemicalFiberStockWarehousingDetailQueryCriteria criteria);

    void update(List<ChemicalFiberStockWarehousingDetail> resources);

    void delete(Integer id);
}
