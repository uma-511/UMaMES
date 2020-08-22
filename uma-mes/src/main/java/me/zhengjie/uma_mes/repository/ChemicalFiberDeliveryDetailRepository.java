package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryDetailRepository extends JpaRepository<ChemicalFiberDeliveryDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryDetail> {

    @Query(value = "SELECT * FROM uma_chemical_fiber_delivery_detail where delivery_note_id = :id",nativeQuery = true)
    List<ChemicalFiberDeliveryDetail> getDetailList(@Param("id") Integer id);



}