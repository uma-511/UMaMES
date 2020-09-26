package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.domain.OverArrearsPayDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author wave
* @date 2020-09-24
*/
public interface OverArrearsPayDetailRepository extends JpaRepository<OverArrearsPayDetail, Integer>, JpaSpecificationExecutor<OverArrearsPayDetail> {

    @Query(value = "select id,price,pay_date,create_date,customer_name,customer_id,enable,create_user FROM uma_over_arrears_pay_detail where customer_id = :id",nativeQuery = true)
    List<OverArrearsPayDetail> getListByCustomerId(@Param("id") Integer id);
}