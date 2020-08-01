package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberProductRepository extends JpaRepository<ChemicalFiberProduct, Integer>, JpaSpecificationExecutor<ChemicalFiberProduct> {
    @Query(value = "SELECT SUM(s.total_number) from uma_chemical_fiber_stock s where s.prod_model=:prodModel ",nativeQuery = true)
    String countStock(@Param("prodModel") String prodModel);
}