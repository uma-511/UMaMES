package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ScanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ScanRecordRepository extends JpaRepository<ScanRecord, Integer>, JpaSpecificationExecutor<ScanRecord> {
}