package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFibeDeviceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
public interface ChemicalFibeDeviceGroupRepository extends JpaRepository<ChemicalFibeDeviceGroup, Integer>, JpaSpecificationExecutor<ChemicalFibeDeviceGroup> {
}