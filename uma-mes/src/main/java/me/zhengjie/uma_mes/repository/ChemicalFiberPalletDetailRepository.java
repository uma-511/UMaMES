package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberPalletDetailRepository extends JpaRepository<ChemicalFiberPalletDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberPalletDetail> {

    @Query(value = "SELECT * FROM uma_chemical_fiber_pallet_detail where pallet_id = :palletNumber and status = 9 or status = 11",nativeQuery = true)
    List<ChemicalFiberPalletDetail> getPalletDateil(@Param("palletNumber") String palletNumber);

    @Query(value = "SELECT * FROM uma_chemical_fiber_pallet_detail where pallet_id = :palletNumber and label_number = :labelNumber",nativeQuery = true)
    ChemicalFiberPalletDetail findByPalletId(@Param("palletNumber") String palletNumber, @Param("labelNumber") String labelNumber);

}