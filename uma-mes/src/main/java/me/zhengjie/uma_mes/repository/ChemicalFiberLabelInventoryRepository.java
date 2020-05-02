package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabelInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-04-14
*/
public interface ChemicalFiberLabelInventoryRepository extends JpaRepository<ChemicalFiberLabelInventory, Integer>, JpaSpecificationExecutor<ChemicalFiberLabelInventory> {
}