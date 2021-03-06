package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.AcidPersionPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-09-05
*/
public interface AcidPersionPerformanceRepository extends JpaRepository<AcidPersionPerformance, Integer>, JpaSpecificationExecutor<AcidPersionPerformance> {
}