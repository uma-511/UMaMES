package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ViewScanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2019-11-26
*/
public interface ViewScanRecordRepository extends JpaRepository<ViewScanRecord, Integer>, JpaSpecificationExecutor<ViewScanRecord> {
}