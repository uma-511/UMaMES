package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.uma_mes.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
public interface ReceiptRepository extends JpaRepository<Receipt, Integer>, JpaSpecificationExecutor<Receipt> {
    @Query(value = "select COUNT(id) FROM uma_receipt WHERE create_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenReceiptCount(@Param("currenDate") String currenDate);

    @Query(value = "select receipt_number FROM uma_receipt WHERE create_date like %:currenDate% ORDER BY create_date desc  LIMIT 1 ",nativeQuery = true)
    String getCurrenReceiptCountWithMaxNumber(@Param("currenDate") String currenDate);

    @Query(value = "SELECT * FROM uma_receipt where customer_id = :currenId and recived_date LIKE %:date%", nativeQuery = true)
    List<Receipt> getReceiptList(@Param("currenId") Integer currenId, @Param("date") String date);
}