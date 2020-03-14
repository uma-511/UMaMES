package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
public interface UmaChemicalFiberStatementRepository extends JpaRepository<UmaChemicalFiberStatement, Integer>, JpaSpecificationExecutor<UmaChemicalFiberStatement> {
}