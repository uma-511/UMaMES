package me.zhengjie.uma_mes.repository;

import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

public interface ChemicalFiberPalletRepository extends JpaRepository<ChemicalFiberPallet, Integer>, JpaSpecificationExecutor<ChemicalFiberPallet> {

    @Query(value = "SELECT count(id) FROM uma_chemical_fiber_pallet where pallet_number like :time%",nativeQuery = true)
    Integer getSize(@Param("time") String time);
}
