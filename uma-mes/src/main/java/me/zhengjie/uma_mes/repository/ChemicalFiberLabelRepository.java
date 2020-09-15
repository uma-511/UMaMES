package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelTotalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberLabelRepository extends JpaRepository<ChemicalFiberLabel, Integer>, JpaSpecificationExecutor<ChemicalFiberLabel> {
    @Query(value = "select max(id) as id,sum(net_weight) as net_weight,sum(fact_per_bag_number) as fact_per_bag_number from uma_chemical_fiber_label where `status`<> 3 and production_id=:productionId GROUP BY production_id",nativeQuery = true)
    Map getTotalByProductionId(@Param("productionId") Integer productionId);

    ChemicalFiberLabel getByLabelNumber(String labelNumber);

    @Query(value = "select * from uma_chemical_fiber_label where machine=:machine order by id desc limit 1",nativeQuery = true)
    ChemicalFiberLabel getLastLabelByMachine(@Param("machine") String machine);

    @Query(value = "select * from uma_chemical_fiber_label  GROUP BY shifts",nativeQuery = true)
    List<ChemicalFiberLabel> getShifts();

}