package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryNoteRepository extends JpaRepository<ChemicalFiberDeliveryNote, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryNote> {
    @Procedure(procedureName = "proc_generate_delivery_detail")
    void deliveryNoteStoredProcedure(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT delivery_note_id as note_id,sum(total_bag) as total_bag, sum(total_weight) as total_weight FROM `uma_chemical_fiber_delivery_detail` GROUP BY delivery_note_id",nativeQuery = true)
    List<Map<String, Object>> getSum();
}