package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.MonthlyWageStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-09-11
*/
public interface MonthlyWageStatisticsRepository extends JpaRepository<MonthlyWageStatistics, Integer>, JpaSpecificationExecutor<MonthlyWageStatistics> {
}