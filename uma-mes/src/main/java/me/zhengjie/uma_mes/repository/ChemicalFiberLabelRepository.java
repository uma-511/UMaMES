package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelTotalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberLabelRepository extends JpaRepository<ChemicalFiberLabel, Integer>, JpaSpecificationExecutor<ChemicalFiberLabel> {
    @Query(value = "select sum(net_weight) as totalNetWeight,sum(fact_per_bag_number) as totalNumber from uma_chemical_fiber_label where `status`<> 3 and production_id=:productionId GROUP BY production_id",nativeQuery = true)
    ChemicalFiberLabelTotalDTO getTotalByProductionId(@Param("productionId") Integer productionId);

}