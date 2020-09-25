package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ChemicalFiberPalletRepository extends JpaRepository<ChemicalFiberPallet, Integer>, JpaSpecificationExecutor<ChemicalFiberPallet> {

    @Query(value = "SELECT count(id) FROM uma_chemical_fiber_pallet where pallet_number like :time%",nativeQuery = true)
    Integer getSize(@Param("time") String time);

    @Query(value = "SELECT\n" +
            "\tpallet_id,\n" +
            "\tsum( CASE WHEN status = 11 OR status = 9 THEN 1 ELSE 0 END ) AS total_bag,\n" +
            "\tsum( CASE WHEN status = 11 OR status = 9 THEN fact_per_bag_number ELSE 0 END ) AS total_number,\n" +
            "\tsum( CASE WHEN status = 11 OR status = 9 THEN gross_weight ELSE 0 END ) AS total_gross_weight,\n" +
            "\tsum( CASE WHEN status = 11 OR status = 9 THEN net_weight ELSE 0 END ) AS total_net_weight,\n" +
            "\tsum( CASE WHEN status = 11 OR status = 9 THEN tare ELSE 0 END ) AS total_tare,\n" +
            "\tsum( CASE WHEN status = 2 THEN 1 ELSE 0 END ) AS warehousing_bag,\n" +
            "\tsum( CASE WHEN status = 2 THEN fact_per_bag_number ELSE 0 END ) AS warehousing_number\n" +
            "FROM\n" +
            "\tuma_chemical_fiber_pallet_detail a\n" +
            "GROUP BY\n" +
            "\tpallet_id",nativeQuery = true)
    List<Map<String, Object>> getNumbar();
}
