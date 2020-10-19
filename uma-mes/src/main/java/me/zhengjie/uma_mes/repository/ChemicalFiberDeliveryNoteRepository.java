package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryNoteRepository extends JpaRepository<ChemicalFiberDeliveryNote, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryNote> {
    @Procedure(procedureName = "proc_generate_delivery_detail")
    void deliveryNoteStoredProcedure(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT count(id) as id , sum(total_price) as total_price  FROM `uma_chemical_fiber_delivery_note` where create_date like %?1%",nativeQuery = true)
    Map<String, Object> getSum(String time);

    @Query(value = "SELECT sum(total_bag) as total_bag, sum(total_weight) as total_weight FROM `uma_chemical_fiber_delivery_detail` where create_date like %?1%",nativeQuery = true)
    Map<String, Object> getSumDelvery(String time);
}
