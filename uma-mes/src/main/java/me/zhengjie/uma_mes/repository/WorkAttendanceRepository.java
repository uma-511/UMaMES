package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.WorkAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wave
* @date 2020-09-07
*/
public interface WorkAttendanceRepository extends JpaRepository<WorkAttendance, Integer>, JpaSpecificationExecutor<WorkAttendance> {
}