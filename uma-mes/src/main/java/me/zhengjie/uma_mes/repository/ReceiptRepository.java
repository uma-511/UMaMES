package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.uma_mes.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
public interface ReceiptRepository extends JpaRepository<Receipt, Integer>, JpaSpecificationExecutor<Receipt> {
    @Query(value = "select COUNT(id) FROM uma_receipt WHERE create_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenReceiptCount(@Param("currenDate") String currenDate);

    @Query(value = "select MAX(receipt_number) FROM uma_receipt WHERE create_date like %:currenDate%",nativeQuery = true)
    String getCurrenReceiptCountWithMaxNumber(@Param("currenDate") String currenDate);
}