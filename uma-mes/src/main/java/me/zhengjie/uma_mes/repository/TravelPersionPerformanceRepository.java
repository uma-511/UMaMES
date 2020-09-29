package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.TravelPersionPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @author wave
* @date 2020-09-02
*/
public interface TravelPersionPerformanceRepository extends JpaRepository<TravelPersionPerformance, Integer>, JpaSpecificationExecutor<TravelPersionPerformance> {

    @Query(value = "delete FROM uma_travel_persion_performance where scan_number=:scanNumber",nativeQuery = true)
    void delByScanNumber(@Param("scanNumber") String scanNumber);
}