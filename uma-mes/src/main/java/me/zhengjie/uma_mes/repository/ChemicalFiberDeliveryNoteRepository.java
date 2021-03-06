package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.math.BigDecimal;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryNoteRepository extends JpaRepository<ChemicalFiberDeliveryNote, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryNote> {
    @Procedure(procedureName = "proc_generate_delivery_detail")
    void deliveryNoteStoredProcedure(@Param("scanNumber") String scanNumber);

    @Query(value = "select user.realname from user where user.username=:username limit 1",nativeQuery = true)
    String getRealNameByUserName(@Param("username") String username);

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_delivery_note WHERE create_date like %:currenDate%  and invalid = 0 and note_status >= 3",nativeQuery = true)
    Integer getCurrenNoteCount(@Param("currenDate") String currenDate);

    @Query(value = "select scan_number FROM uma_chemical_fiber_delivery_note WHERE create_date like %:currenDate%  \n" +
            "ORDER BY create_date desc  LIMIT 1 ",nativeQuery = true)
    String getCurrenNoteCountWithMaxNumber(@Param("currenDate") String currenDate);

    @Query(value = "select * FROM uma_chemical_fiber_delivery_note where scan_number = :scanNumber",nativeQuery = true)
    ChemicalFiberDeliveryNote getByScanNumber(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT  SUM(b.total_number) FROM uma_chemical_fiber_delivery_note a LEFT JOIN  uma_chemical_fiber_delivery_detail b on a.scan_number = b.scan_number where a.create_date LIKE %:dateTime% and b.unit = :unit and invalid = 0  and a.note_status >= 3",nativeQuery = true)
    BigDecimal getTonnageInStorage(@Param("dateTime") String dateTime, @Param("unit") String unit);

    @Query(value = "SELECT SUM(total_price) from uma_chemical_fiber_delivery_note where create_date LIKE %:dateTime% and invalid = 0 and note_status >= 3",nativeQuery = true)
    BigDecimal getDeliveryAmount(@Param("dateTime") String dateTime);

    @Query(value = "SELECT sum(b.total_price) as k FROM uma_chemical_fiber_delivery_note b WHERE b.customer_id = :customerId AND b.delivery_date > '1950-01' AND delivery_date < :dateTime ",nativeQuery = true)
    Integer getOnCreditSum(@Param("dateTime") String dateTime, @Param("customerId") Integer customerId);

    @Query(value = "select user.id from user where user.realname=:username limit 1",nativeQuery = true)
    Integer getIdByRealName(@Param("username") String username);

    @Query(value = "SELECT r.permission FROM role r WHERE r.id = (select ur.role_id  from users_roles ur where ur.user_id =(select u.id from user u where u.realname=:username)) limit 1",nativeQuery = true)
    String getPermissionByRealName(@Param("username") String username);

    @Query(value = "select j.`name` from job j where j.id=(select u.job_id from `user` u where u.id= :id) limit 1",nativeQuery = true)
    String getPermissionByUserId(Long id);

    @Query(value = "SELECT n.customer_name FROM uma_chemical_fiber_delivery_note n where n.scan_number=:scanNumber limit 1",nativeQuery = true)
    String getCusotmerNameByScanNumber(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT n.start_place FROM uma_chemical_fiber_delivery_note n where n.scan_number=:scanNumber limit 1",nativeQuery = true)
    String getStartPlaceByScanNumber(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT n.end_place FROM uma_chemical_fiber_delivery_note n where n.scan_number=:scanNumber limit 1",nativeQuery = true)
    String getEndPlaceByScanNumber(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT sum(real_quantity) FROM `uma_chemical_fiber_delivery_detail` where scan_number=:scanNumber and unit = '吨'",nativeQuery = true)
    String getReferenceQuantityByScanNumberWhithTon(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT sum(real_quantity) FROM `uma_chemical_fiber_delivery_detail` where scan_number=:scanNumber and unit = '箱'",nativeQuery = true)
    String getReferenceQuantityByScanNumberWhithBox(@Param("scanNumber") String scanNumber);

    @Query(value = "SELECT sum(real_quantity) FROM `uma_chemical_fiber_delivery_detail` where scan_number=:scanNumber and unit = '支'",nativeQuery = true)
    String getReferenceQuantityByScanNumberWhithBottle(@Param("scanNumber") String scanNumber);


}