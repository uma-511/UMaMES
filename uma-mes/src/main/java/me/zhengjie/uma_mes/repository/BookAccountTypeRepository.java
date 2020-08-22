package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.BookAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
public interface BookAccountTypeRepository extends JpaRepository<BookAccountType, Integer>, JpaSpecificationExecutor<BookAccountType> {
}