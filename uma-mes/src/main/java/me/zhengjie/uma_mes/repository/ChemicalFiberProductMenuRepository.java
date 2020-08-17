package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberProductMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChemicalFiberProductMenuRepository extends JpaRepository<ChemicalFiberProductMenu, Integer>, JpaSpecificationExecutor<ChemicalFiberProductMenu> {

}
