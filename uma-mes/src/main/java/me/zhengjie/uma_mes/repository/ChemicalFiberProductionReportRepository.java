package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberProductionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface ChemicalFiberProductionReportRepository extends JpaRepository<ChemicalFiberProductionReport, Integer>, JpaSpecificationExecutor<ChemicalFiberProductionReport> {

    @Query(value = "select * from uma_chemical_fiber_production_report where time BETWEEN :Date1 AND :Date2  and shifts = :shifts and machine = :machine and production_id = :prodctionId",nativeQuery = true)
    ChemicalFiberProductionReport getReport(@Param("Date1") String Date1,@Param("Date2") String Date2,@Param("shifts")  String shifts, @Param("machine") String machine, @Param("prodctionId") Integer prodctionId);

}
