package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberStockRepository extends JpaRepository<ChemicalFiberStock, Integer>, JpaSpecificationExecutor<ChemicalFiberStock> {
    @Procedure(procedureName = "proc_update_stock")
    void stockTask();

    ChemicalFiberStock findByProdColorAndProdFineness(String color,String fineness);

    @Query(value = "SELECT sum(total_bag) as bag, sum(total_net_weight) as net_weight, sum(total_gross_weight) as gross_weight FROM `uma_chemical_fiber_stock`",nativeQuery = true)
    Map<String, Object> getSumStock();
}