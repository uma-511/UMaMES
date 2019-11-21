package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ScanRecordLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Tan Jun Ming
* @date 2019-11-21
*/
public interface ScanRecordLabelRepository extends JpaRepository<ScanRecordLabel, Integer>, JpaSpecificationExecutor<ScanRecordLabel> {
}