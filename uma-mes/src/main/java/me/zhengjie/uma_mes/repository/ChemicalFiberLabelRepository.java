package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelTotalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
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

    @Query(value = "select * from uma_chemical_fiber_label WHERE print_time BETWEEN ?1 AND ?2 GROUP BY shifts",nativeQuery = true)
    List<ChemicalFiberLabel> getShiftsList(String stater, String end);

    @Query(value = "select * from uma_chemical_fiber_label WHERE print_time BETWEEN ?1 AND ?2 GROUP BY machine",nativeQuery = true)
    List<ChemicalFiberLabel> getMachine(String stater, String end);

    @Query(value = "select shifts, machine, production_id as prodctionNumber from uma_chemical_fiber_label WHERE print_time BETWEEN ?1 AND ?2 GROUP BY shifts, machine, production_id",nativeQuery = true)
    List<Map<String, Object>> gets(String stater, String end);

    @Query(value = "select max(print_time) as prodctionNumber from uma_chemical_fiber_label",nativeQuery = true)
    Timestamp getTime();

    @Query(value = "select * from uma_chemical_fiber_label WHERE print_time BETWEEN ?1 AND ?2 GROUP BY production_id",nativeQuery = true)
    List<ChemicalFiberLabel> getProductionId(String stater, String end);

    @Query(value = "SELECT sum( CASE WHEN `status` != 3 and status != 0 THEN 1 ELSE 0 END ) AS total_bag, \n" +
            "sum( CASE WHEN `status` != 3 and status != 0 THEN net_weight ELSE 0 END ) AS net_weight,\n" +
            "sum( CASE WHEN `status` != 3 and status != 0 THEN gross_weight ELSE 0 END ) AS gross_weight FROM `uma_chemical_fiber_label` where print_time like %?1%",nativeQuery = true)
    Map<String, Object> getSumLabel(String time);



}
