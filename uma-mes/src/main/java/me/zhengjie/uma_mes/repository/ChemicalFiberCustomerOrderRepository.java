package me.zhengjie.uma_mes.repository;


import me.zhengjie.uma_mes.domain.ChemicalFiberCustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
public interface ChemicalFiberCustomerOrderRepository extends JpaRepository<ChemicalFiberCustomerOrder, Integer>, JpaSpecificationExecutor<ChemicalFiberCustomerOrder> {

    @Query(value = "SELECT * FROM `uma_chemical_fiber_customer_order` where warehousing_date like %?1%",nativeQuery = true)
    List<ChemicalFiberCustomerOrder> get(String time);
}
