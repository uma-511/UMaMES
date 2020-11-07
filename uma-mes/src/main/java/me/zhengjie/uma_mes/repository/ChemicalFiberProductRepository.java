package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberProductRepository extends JpaRepository<ChemicalFiberProduct, Integer>, JpaSpecificationExecutor<ChemicalFiberProduct> {

    @Query(value = "SELECT * FROM `uma_chemical_fiber_product` where color like %?1% and del_flag = 0 GROUP BY color",nativeQuery = true)
    List<ChemicalFiberProduct> getColor(String color);

    @Query(value = "SELECT * FROM `uma_chemical_fiber_product` where fineness like %?1% and del_flag = 0 GROUP BY fineness",nativeQuery = true)
    List<ChemicalFiberProduct> getFineness(String fineness);

    @Query(value = "SELECT count(id) FROM `uma_chemical_fiber_product`",nativeQuery = true)
    Integer getMax();
}
