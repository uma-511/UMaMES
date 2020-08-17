package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventory;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventoryDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDetailDTO;

import java.util.List;

public interface ChemicalFiberStockLnventoryDetailService {

    List<ChemicalFiberStockLnventoryDetailDTO> queryAll();

    List<ChemicalFiberStockLnventoryDetailDTO> create(List<ChemicalFiberStockLnventoryDetail> resources);

    List<ChemicalFiberStockLnventoryDetailDTO> queryAllList(ChemicalFiberStockLnventory resources);

    void update(List<ChemicalFiberStockLnventoryDetail> resources);

}
