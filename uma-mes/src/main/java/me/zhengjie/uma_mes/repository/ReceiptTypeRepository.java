package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ReceiptType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
public interface ReceiptTypeRepository extends JpaRepository<ReceiptType, Integer>, JpaSpecificationExecutor<ReceiptType> {
}