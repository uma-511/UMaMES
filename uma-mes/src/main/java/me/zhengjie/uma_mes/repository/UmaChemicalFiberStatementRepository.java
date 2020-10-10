package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
public interface UmaChemicalFiberStatementRepository extends JpaRepository<UmaChemicalFiberStatement, Integer>, JpaSpecificationExecutor<UmaChemicalFiberStatement> {

    @Query(value = "select * FROM uma_chemical_fiber_statement where customer_id = :customerId and create_date between :createDateStart and :createDateEnd",nativeQuery = true)
    UmaChemicalFiberStatement getOneId(@Param("customerId") Integer customerId, @Param("createDateStart") String createDateStart, @Param("createDateEnd") String createDateEnd );

    @Query(value = "select COUNT(id) FROM uma_chemical_fiber_statement WHERE up_date like %:currenDate%",nativeQuery = true)
    Integer getCurrenNoteCount(@Param("currenDate") String currenDate);


    /*@Query(value = "SELECT a.id, a.account_code, a.create_date, a.create_user, a.customer_id, a.customer_name, a.contacts, a.contact_phone, a.receivable, a.accumulated_arrears, a.up_date, (SELECT sum(b.total_price) as k FROM uma_chemical_fiber_delivery_note b WHERE b.customer_id = a.customer_id AND b.delivery_date > '1950-01' AND b.delivery_date <= a.up_date AND b.invalid = 0) as total_arrears FROM uma_chemical_fiber_statement a where  a.customer_name like %:customerName% and a.account_code like %:accountCode% and a.create_date like %:createDate% ORDER BY a.id DESC LIMIT :Start,:End",nativeQuery = true)
    List<UmaChemicalFiberStatement> findadd(@Param("Start") Integer Start, @Param("End") Integer End, @Param("customerName") String customerName,  @Param("accountCode") String accountCode, @Param("createDate") String createDate);*/

    @Query(value = "SELECT * FROM uma_chemical_fiber_statement a where  a.customer_name like %:customerName% and a.account_code like %:accountCode% and a.up_date like %:createDate% ORDER BY a.id DESC LIMIT :Start,:End",nativeQuery = true)
    List<UmaChemicalFiberStatement> findadd(@Param("Start") Integer Start, @Param("End") Integer End, @Param("customerName") String customerName,  @Param("accountCode") String accountCode, @Param("createDate") String createDate);

    @Query(value = "SELECT COUNT(id) FROM uma_chemical_fiber_statement where customer_name like %:customerName% and account_code like %:accountCode% and up_date like %:createDate%",nativeQuery = true)
    Integer findSize(@Param("customerName") String customerName,  @Param("accountCode") String accountCode, @Param("createDate") String createDate);


}