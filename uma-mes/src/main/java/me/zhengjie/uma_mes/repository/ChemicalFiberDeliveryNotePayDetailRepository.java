package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-07-29
*/
public interface ChemicalFiberDeliveryNotePayDetailRepository extends JpaRepository<ChemicalFiberDeliveryNotePayDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryNotePayDetail> {
}