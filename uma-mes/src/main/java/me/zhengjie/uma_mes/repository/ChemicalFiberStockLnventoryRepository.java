package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ChemicalFiberStockLnventoryRepository extends JpaRepository<ChemicalFiberStockLnventory, Integer>, JpaSpecificationExecutor<ChemicalFiberStockLnventory> {

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_stock_lnventory WHERE create_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenNoteCount(@Param("currenDate") String currenDate);

    @Query(value = "select max(lnventory_number) FROM uma_chemical_fiber_stock_lnventory",nativeQuery = true)
    String getScanNumberMax();

    @Query(value = "select user.realname from user where user.username=:username limit 1",nativeQuery = true)
    String getRealNameByUserName(@Param("username") String username);
}
