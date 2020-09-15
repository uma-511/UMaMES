package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ScanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ScanRecordRepository extends JpaRepository<ScanRecord, Integer>, JpaSpecificationExecutor<ScanRecord> {


    @Query(value = "Select * from uma_scan_record where scan_time like ?1% and type = ?2",nativeQuery = true)
    List<ScanRecord> getTime(String time, String type);
}