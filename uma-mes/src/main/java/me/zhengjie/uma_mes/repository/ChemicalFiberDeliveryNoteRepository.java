package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryNoteRepository extends JpaRepository<ChemicalFiberDeliveryNote, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryNote> {
    @Procedure(procedureName = "proc_generate_delivery_detail")
    void deliveryNoteStoredProcedure(@Param("scanNumber") String scanNumber);

    @Query(value = "select user.realname from user where user.username=:username limit 1",nativeQuery = true)
    String getRealNameByUserName(@Param("username") String username);

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_delivery_note WHERE create_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenNoteCount(@Param("currenDate") String currenDate);

    @Query(value = "select scan_number FROM uma_chemical_fiber_delivery_note WHERE create_date like %:currenDate%  \n" +
            "ORDER BY create_date desc  LIMIT 1 ",nativeQuery = true)
    String getCurrenNoteCountWithMaxNumber(@Param("currenDate") String currenDate);

    @Query(value = "select * FROM uma_chemical_fiber_delivery_note where scan_number = :scanNumber",nativeQuery = true)
    ChemicalFiberDeliveryNote getByScanNumber(@Param("scanNumber") String scanNumber);
}