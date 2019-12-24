package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberProductionRepository extends JpaRepository<ChemicalFiberProduction, Integer>, JpaSpecificationExecutor<ChemicalFiberProduction> {
    List<ChemicalFiberProduction> findByProdIdAndMachineNumber(Integer prodId, String machineNumber);
}