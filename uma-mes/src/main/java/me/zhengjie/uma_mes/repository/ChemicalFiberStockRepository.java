package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberStockRepository extends JpaRepository<ChemicalFiberStock, Integer>, JpaSpecificationExecutor<ChemicalFiberStock> {

    @Procedure(procedureName = "proc_update_stock")
    void stockTask();

    ChemicalFiberStock findByProdColorAndProdFineness(String color,String fineness);

    @Query(value = "select * from uma_chemical_fiber_stock where prod_name like %:innerName% or prod_model like %:innerName%",nativeQuery = true)
    List<ChemicalFiberStock> querySelectList(@Param("innerName") String innerName);

    @Query(value = "select * from uma_chemical_fiber_stock where prod_name =:prodName ",nativeQuery = true)
    ChemicalFiberStock findByProdName(@Param("prodName") String prodName);

    @Query(value = "select * from uma_chemical_fiber_stock where prod_id =:prodId and prod_unit=:prodUnit",nativeQuery = true)
    ChemicalFiberStock findByProdId(@Param("prodId") Integer prodId, @Param("prodUnit") String prodUnit);

    @Query(value = "SELECT SUM(total_number) FROM uma_chemical_fiber_stock where prod_unit = :unit",nativeQuery = true)
    BigDecimal getTonnageInStorage(@Param("unit") String unit);
}