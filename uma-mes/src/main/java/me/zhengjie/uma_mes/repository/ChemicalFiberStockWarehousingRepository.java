package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChemicalFiberStockWarehousingRepository extends JpaRepository<ChemicalFiberStockWarehousing, Integer>, JpaSpecificationExecutor<ChemicalFiberStockWarehousing> {

    @Query(value = "select user.realname from user where user.username=:username limit 1",nativeQuery = true)
    String getRealNameByUserName(@Param("username") String username);

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_stock_warehousing WHERE create_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenNoteCount(@Param("currenDate") String currenDate);

    @Query(value = "select max(scan_number) FROM uma_chemical_fiber_stock_warehousing",nativeQuery = true)
    String getScanNumberMax();

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_stock_warehousing WHERE warehousing_date like %:dateTime%",nativeQuery = true)
    Integer getWarehousingOrders(@Param("dateTime") String dateTime);

    @Query(value = "SELECT  SUM(b.total_price) FROM uma_chemical_fiber_stock_warehousing a LEFT JOIN  uma_chemical_fiber_stock_warehousing_detail b on a.id = b.warehousing_id where warehousing_date LIKE %:dateTime% and b.unit = :unit",nativeQuery = true)
    Integer getTonnageInStorage(@Param("dateTime") String dateTime, @Param("unit") String unit);

    @Query(value = "SELECT b.id from uma_chemical_fiber_stock_warehousing b where b.invalid = 0 and b.warehousing_status = 2 and  supplier_name like %:name% and create_user like %:user% ",nativeQuery = true)
    List<Integer> getNotId(@Param("user") String user, @Param("name") String name);



}
