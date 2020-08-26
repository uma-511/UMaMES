package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventoryDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChemicalFiberStockLnventoryDetailRepository extends JpaRepository<ChemicalFiberStockLnventoryDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberStockLnventoryDetail> {


    @Query(value = "select * from uma_chemical_fiber_stock_lnventory_detail where lnventory_id = :id",nativeQuery = true)
    List<ChemicalFiberStockLnventoryDetail> getDetaList(@Param("id") Integer id);

    @Modifying
    @Query(value = "DELETE FROM uma_chemical_fiber_stock_lnventory_detail where lnventory_id = :id",nativeQuery = true)
    void deleteByLnventoryId(@Param("id") Integer id);


}
