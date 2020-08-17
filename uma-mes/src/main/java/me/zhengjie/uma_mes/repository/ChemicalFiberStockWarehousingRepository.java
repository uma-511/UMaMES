package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ChemicalFiberStockWarehousingRepository extends JpaRepository<ChemicalFiberStockWarehousing, Integer>, JpaSpecificationExecutor<ChemicalFiberStockWarehousing> {

    @Query(value = "select user.realname from user where user.username=:username limit 1",nativeQuery = true)
    String getRealNameByUserName(@Param("username") String username);

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_stock_warehousing WHERE create_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenNoteCount(@Param("currenDate") String currenDate);

    @Query(value = "select max(scan_number) FROM uma_chemical_fiber_stock_warehousing",nativeQuery = true)
    String getScanNumberMax();
}
