package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.WorkAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @author wave
* @date 2020-09-07
*/
public interface WorkAttendanceRepository extends JpaRepository<WorkAttendance, Integer>, JpaSpecificationExecutor<WorkAttendance> {
    @Query(value = "select serial_number FROM uma_work_attendance WHERE create_date like %:currenDate%  \n" +
            "ORDER BY create_date desc  LIMIT 1 ",nativeQuery = true)
    String getCurrenAttendanceCountWithMaxNumber(@Param("currenDate") String currenDate);

}