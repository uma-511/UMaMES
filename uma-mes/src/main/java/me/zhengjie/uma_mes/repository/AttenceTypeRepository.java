package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.AttenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-09-08
*/
public interface AttenceTypeRepository extends JpaRepository<AttenceType, Integer>, JpaSpecificationExecutor<AttenceType> {
}