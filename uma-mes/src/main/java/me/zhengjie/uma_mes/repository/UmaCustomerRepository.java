package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.UmaCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaCustomerRepository extends JpaRepository<UmaCustomer, Integer>, JpaSpecificationExecutor<UmaCustomer> {
}