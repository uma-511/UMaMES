package me.zhengjie.uma_mes.repository;

import com.lgmn.common.domain.LgmnPage;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberProductionRepository extends JpaRepository<ChemicalFiberProduction, Integer>, JpaSpecificationExecutor<ChemicalFiberProduction> {
    List<ChemicalFiberProduction> findByProdIdAndMachineNumber(Integer prodId, String machineNumber);

   /* @Query(value = "select *, " +
            "case when temp.in_stock_number / (temp.in_stock_number +temp.out_stock_number) is null then 0 else convert(temp.in_stock_number / (temp.in_stock_number +temp.out_stock_number),decimal(10,2)) end as in_stock_prop, " +
            "case when temp.in_net_weight / (temp.in_net_weight+temp.out_net_weight) is null then 0 else convert(temp.in_net_weight / (temp.in_net_weight+temp.out_net_weight),decimal(10,2)) end as in_weight_prop " +
            "from (" +
            "select product.*,sum(case when label.`status`=1 then 1 else 0 end) as `in_stock_pack`,sum(case when label.`status`=1 then label.fact_per_bag_number else 0 end) as `in_stock_number`," +
            "sum(case when label.`status`=1 then label.net_weight else 0 end) as `in_net_weight`,sum(case when label.`status`=1 then label.gross_weight else 0 end) as `in_gross_weight`," +
            "sum(case when label.`status`=2 then 1 else 0 end) as `out_stock_pack`,sum(case when label.`status`=2 then label.fact_per_bag_number else 0 end) as `out_stock_number`," +
            "sum(case when label.`status`=2 then label.net_weight else 0 end) as `out_net_weight`,sum(case when label.`status`=2 then label.gross_weight else 0 end) as `out_gross_weight`," +
            "sum(case when label.`status`=3 then 1 else 0 end) as `cancel_stock_pack`,sum(case when label.`status`=3 then label.fact_per_bag_number else 0 end) as `cancel_stock_number`," +
            "sum(case when label.`status`=3 then label.net_weight else 0 end) as `cancel_net_weight`,sum(case when label.`status`=3 then label.gross_weight else 0 end) as `cancel_gross_weight`" +
            "from uma_chemical_fiber_label label LEFT JOIN uma_chemical_fiber_product product on label.product_id=product.id " +
            "where label.print_time between ?1 and ?2 " +
            "group by product.id" +
            ") as temp where temp.del_flag = 0 and temp.color like %?3% and temp.fineness like %?4%",
            countQuery = "select count(*) as totalElements " +
                    "from (" +
                    "select product.*,sum(case when label.`status`=1 then 1 else 0 end) as `in_stock_pack`,sum(case when label.`status`=1 then label.fact_per_bag_number else 0 end) as `in_stock_number`," +
                    "sum(case when label.`status`=1 then label.net_weight else 0 end) as `in_net_weight`,sum(case when label.`status`=1 then label.gross_weight else 0 end) as `in_gross_weight`," +
                    "sum(case when label.`status`=2 then 1 else 0 end) as `out_stock_pack`,sum(case when label.`status`=2 then label.fact_per_bag_number else 0 end) as `out_stock_number`," +
                    "sum(case when label.`status`=2 then label.net_weight else 0 end) as `out_net_weight`,sum(case when label.`status`=2 then label.gross_weight else 0 end) as `out_gross_weight`," +
                    "sum(case when label.`status`=3 then 1 else 0 end) as `cancel_stock_pack`,sum(case when label.`status`=3 then label.fact_per_bag_number else 0 end) as `cancel_stock_number`, " +
                    "sum(case when label.`status`=3 then label.net_weight else 0 end) as `cancel_net_weight`,sum(case when label.`status`=3 then label.gross_weight else 0 end) as `cancel_gross_weight`" +
                    "from uma_chemical_fiber_label label LEFT JOIN uma_chemical_fiber_product product on label.product_id=product.id " +
                    "where label.print_time between ?1 and ?2 " +
                    "group by product.id " +
                    ") as temp where temp.del_flag = 0 and temp.color like %?3% and temp.fineness like %?4%",
            nativeQuery = true)
    Page<Map<String, Object>> getProductionReport(String data1, String data2, String color, String fineness, Pageable tempPageable);*/

    @Query(value = "SELECT * " +
            "FROM (" +
            "SELECT product.*,label.shifts, production.number, production.machine_number," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE 1 END ) AS `in_stock_pack`," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.fact_per_bag_number END ) AS `in_stock_number`," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.net_weight END ) AS `in_net_weight`," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.gross_weight END ) AS `in_gross_weight`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE 1 END ) AS `out_stock_pack`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.fact_per_bag_number END ) AS `out_stock_number`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.net_weight END ) AS `out_net_weight`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.gross_weight END ) AS `out_gross_weight`" +
            "FROM uma_chemical_fiber_label label LEFT JOIN uma_chemical_fiber_product product ON label.product_id = product.id Left join uma_chemical_fiber_production production on label.production_id = production.id WHERE label.print_time BETWEEN ?1 AND ?2 " +
            "GROUP BY product.id" +
            ") AS temp WHERE temp.del_flag = 0 AND temp.color LIKE %?3% AND temp.fineness LIKE %?4%",
            countQuery = "select count(*) as totalElements " +
                    "from (" +
                    "SELECT product.*," +
                    "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE 1 END ) AS `in_stock_pack`," +
                    "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.fact_per_bag_number END ) AS `in_stock_number`," +
                    "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.net_weight END ) AS `in_net_weight`," +
                    "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.gross_weight END ) AS `in_gross_weight`," +
                    "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE 1 END ) AS `out_stock_pack`," +
                    "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.fact_per_bag_number END ) AS `out_stock_number`," +
                    "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.net_weight END ) AS `out_net_weight`," +
                    "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.gross_weight END ) AS `out_gross_weight`" +
                    "FROM uma_chemical_fiber_label label LEFT JOIN uma_chemical_fiber_product product ON label.product_id = product.id where label.print_time BETWEEN ?1 AND ?2 " +
                    "GROUP BY product.id" +
                    ") AS temp WHERE temp.del_flag = 0 AND temp.color LIKE %?3% AND temp.fineness LIKE %?4%",
            nativeQuery = true)
    Page<Map<String, Object>> getProductionReport(String data1, String data2, String color, String fineness, Pageable tempPageable);

    @Query(value = "SELECT * " +
            "FROM (" +
            "SELECT product.*,label.shifts, production.number, production.machine_number," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE 1 END ) AS `in_stock_pack`," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.fact_per_bag_number END ) AS `in_stock_number`," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.net_weight END ) AS `in_net_weight`," +
            "sum( CASE WHEN label.`status` = 4 THEN 0 ELSE label.gross_weight END ) AS `in_gross_weight`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE 1 END ) AS `out_stock_pack`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.fact_per_bag_number END ) AS `out_stock_number`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.net_weight END ) AS `out_net_weight`," +
            "sum( CASE WHEN label.`status` = 0 OR label.`status` = 4 THEN 0 ELSE label.gross_weight END ) AS `out_gross_weight`" +
            "FROM uma_chemical_fiber_label label LEFT JOIN uma_chemical_fiber_product product ON label.product_id = product.id Left join uma_chemical_fiber_production production on label.production_id = production.id WHERE label.print_time BETWEEN ?1 AND ?2 " +
            "GROUP BY product.id" +
            ") AS temp WHERE temp.del_flag = 0 AND temp.color LIKE %?3% AND temp.fineness LIKE %?4%",
            nativeQuery = true)
    List<Map<String, Object>> getProductionReportSummaries(String data1, String data2, String color, String fineness);
}