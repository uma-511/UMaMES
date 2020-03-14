package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatementDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
public interface UmaChemicalFiberStatementDetailsRepository extends JpaRepository<UmaChemicalFiberStatementDetails, Integer>, JpaSpecificationExecutor<UmaChemicalFiberStatementDetails> {
}