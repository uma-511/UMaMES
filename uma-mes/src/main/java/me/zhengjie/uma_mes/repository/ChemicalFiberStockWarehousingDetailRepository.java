package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChemicalFiberStockWarehousingDetailRepository extends JpaRepository<ChemicalFiberStockWarehousingDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberStockWarehousingDetail> {
}
