package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author Xie Ji Biao
* @date 2020-08-08
*/
public interface ChemicalFiberDeliveryNotePayDetailRepository extends JpaRepository<ChemicalFiberDeliveryNotePayDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryNotePayDetail> {
    @Query(value = "select * FROM uma_chemical_fiber_delivery_note_pay_detail where scan_number = :scanNumber",nativeQuery = true)
    List<ChemicalFiberDeliveryNotePayDetail> getListByScanNumber(@Param("scanNumber") String scanNumber);
}