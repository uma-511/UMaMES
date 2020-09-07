package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberPalletDetailRepository extends JpaRepository<ChemicalFiberPalletDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberPalletDetail> {

}