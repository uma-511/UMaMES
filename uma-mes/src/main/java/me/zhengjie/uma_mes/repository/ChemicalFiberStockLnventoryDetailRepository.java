package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventoryDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChemicalFiberStockLnventoryDetailRepository extends JpaRepository<ChemicalFiberStockLnventoryDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberStockLnventoryDetail> {
}
