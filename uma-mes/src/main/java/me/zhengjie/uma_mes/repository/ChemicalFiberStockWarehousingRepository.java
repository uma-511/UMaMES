package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ChemicalFiberStockWarehousingRepository extends JpaRepository<ChemicalFiberStockWarehousing, Integer>, JpaSpecificationExecutor<ChemicalFiberStockWarehousing> {

    @Query(value = "select user.realname from user where user.username=:username limit 1",nativeQuery = true)
    String getRealNameByUserName(@Param("username") String username);

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_stock_warehousing WHERE create_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenNoteCount(@Param("currenDate") String currenDate);

    @Query(value = "select max(scan_number) FROM uma_chemical_fiber_stock_warehousing",nativeQuery = true)
    String getScanNumberMax();

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_stock_warehousing WHERE warehousing_date like %:dateTime% and invalid = 0 and warehousing_status = 2",nativeQuery = true)
    BigDecimal getWarehousingOrders(@Param("dateTime") String dateTime);

    @Query(value = "SELECT  SUM(b.warehousing_number) FROM uma_chemical_fiber_stock_warehousing a LEFT JOIN  uma_chemical_fiber_stock_warehousing_detail b on a.id = b.warehousing_id where warehousing_date LIKE %:dateTime% and b.unit = :unit and a.invalid = 0 and a.warehousing_status = 2",nativeQuery = true)
    BigDecimal getTonnageInStorage(@Param("dateTime") String dateTime, @Param("unit") String unit);

    @Query(value = "SELECT b.id from uma_chemical_fiber_stock_warehousing b where b.invalid = 0 and b.warehousing_status = 2 and  supplier_name like %:name% and create_user like %:user% ",nativeQuery = true)
    List<Integer> getNotId(@Param("user") String user, @Param("name") String name);

    @Query(value = "select a.scan_number, a.warehousing_date, a.supplier_name, b.prod_name, b.unit, b.warehousing_number, b.price, b.total_price, a.create_user, a.create_date\n" +
            "from uma_chemical_fiber_stock_warehousing a join uma_chemical_fiber_stock_warehousing_detail b on a.id = b.warehousing_id where a.invalid = 0 and a.warehousing_status = 2 \n"+
            "and a.create_date between ?1 and ?2 and a.scan_number like %?3% and b.prod_name like %?4% and a.create_user like %?5% and a.supplier_name like %?6% ORDER BY a.warehousing_date desc",nativeQuery = true)
    List<Map<String, Object>>  getReort(String data1, String data2, String scanNumber, String prodName, String createUser, String supplierName);
}
