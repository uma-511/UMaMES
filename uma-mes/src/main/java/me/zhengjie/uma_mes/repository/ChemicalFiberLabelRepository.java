package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberLabelRepository extends JpaRepository<ChemicalFiberLabel, Integer>, JpaSpecificationExecutor<ChemicalFiberLabel> {
}