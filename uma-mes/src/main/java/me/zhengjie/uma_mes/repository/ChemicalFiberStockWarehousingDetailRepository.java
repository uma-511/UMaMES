package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ChemicalFiberStockWarehousingDetailRepository extends JpaRepository<ChemicalFiberStockWarehousingDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberStockWarehousingDetail> {

    @Query(value = "SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\tdeta.*, war.supplier_name, war.create_user\n" +
            "FROM\n" +
            "\tuma_chemical_fiber_stock_warehousing_detail deta\n" +
            "\tLEFT JOIN uma_chemical_fiber_stock_warehousing war ON deta.warehousing_id = war.id\n" +
            "\twhere war.invalid = 0 and war.create_date between ?1 and ?2 \n" +
            "\t) AS temp where temp.invalid = 0 ",
            nativeQuery = true)
    List<Map<String, Object>> getSummaryDate(String data1, String data2, String color, String fineness);

    @Query(value = "select * from(SELECT warehousing_id , sum( CASE WHEN unit = '吨' THEN warehousing_number ELSE 0 END ) AS ton, sum( CASE WHEN unit = '支' THEN warehousing_number ELSE 0 END ) AS branch FROM uma_chemical_fiber_stock_warehousing_detail GROUP BY warehousing_id ) as temp ",nativeQuery = true)
    List<Map<String, Object>> getTonAndBranch();

    @Query(value = "select sum( CASE WHEN unit = '吨' THEN warehousing_number ELSE 0 END ) AS ton, sum( CASE WHEN unit = '支' THEN warehousing_number ELSE 0 END ) AS branch from uma_chemical_fiber_stock_warehousing a join uma_chemical_fiber_stock_warehousing_detail b on a.id = b.warehousing_id where a.invalid = 0 GROUP BY unit",nativeQuery = true)
    Map<String, Object> getSumTonAndBranch();


}
