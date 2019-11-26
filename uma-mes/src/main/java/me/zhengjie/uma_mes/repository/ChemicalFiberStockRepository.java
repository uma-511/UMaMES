package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberStockRepository extends JpaRepository<ChemicalFiberStock, Integer>, JpaSpecificationExecutor<ChemicalFiberStock> {
    @Procedure(procedureName = "proc_update_stock")
    void stockTask();
}