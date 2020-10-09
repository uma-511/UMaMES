package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.Customer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    @Query(value = "SELECT customer.id,customer.NAME,customer.CODE,customer.address,customer.contacts,customer.contact_phone,customer.remark,customer.create_date,customer.create_user,customer.del_flag,customer.full_name,customer.account,customer.over_arrears,customer.reconciliation," +
            " (SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.create_date < :startTime) as total_arrears" +
            ",(SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.create_date BETWEEN :startTime and :endTime) as current_arrears" +
            " FROM uma_customer customer" +
            " where 1=1 " +
            " and del_flag = 0 and customer.NAME like %:name% and customer.CODE like %:code% and customer.address like %:address% and customer.contacts like %:contacts% and customer.contact_phone like %:contactPhone%" +
            " ORDER BY customer.id DESC LIMIT :Start,:End",nativeQuery = true)
    List<Customer> findAllWithTotalArrears(@Param("Start") Integer Start, @Param("End") Integer End, @Param("name") String name, @Param("code") String code, @Param("address") String address, @Param("contacts") String contacts, @Param("contactPhone") String contactPhone, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT customer.id,customer.NAME,customer.CODE,customer.address,customer.contacts,customer.contact_phone,customer.remark,customer.create_date,customer.create_user,customer.del_flag,customer.full_name,customer.account,customer.over_arrears,customer.reconciliation," +
            " (SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.create_date < :startTime) as total_arrears" +
            ",(SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.create_date BETWEEN :startTime and :endTime) as current_arrears" +
            " FROM uma_customer customer" +
            " where 1=1 " +
            " and del_flag = 0 and customer.NAME like %:name% and customer.CODE like %:code% and customer.address like %:address% and customer.contacts like %:contacts% and customer.contact_phone like %:contactPhone%" +
            " ORDER BY customer.id ",nativeQuery = true)
    List<Customer> findAllWithTotalArrearsForGetList(@Param("name") String name, @Param("code") String code, @Param("address") String address, @Param("contacts") String contacts, @Param("contactPhone") String contactPhone, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT customer.id,customer.NAME,customer.CODE,customer.address,customer.contacts,customer.contact_phone,customer.remark,customer.create_date,customer.create_user,customer.del_flag,customer.full_name,customer.account,customer.over_arrears,customer.reconciliation," +
            " (SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.create_date < :startTime) as total_arrears" +
            ",(SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.create_date BETWEEN :startTime and :endTime) as current_arrears" +
            " FROM uma_customer customer" +
            " where 1=1 and del_flag = 0 and id = :id",nativeQuery = true)
    Customer findByIdWithArrears(@Param("id") Integer id, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT customer.id,customer.NAME,customer.CODE,customer.address,customer.contacts,customer.contact_phone,customer.remark,customer.create_date,customer.create_user,customer.del_flag,customer.full_name,customer.account,customer.over_arrears,customer.reconciliation," +
            " (SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.delivery_date < :startTime) as total_arrears" +
            ",(SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.delivery_date BETWEEN :startTime and :endTime) as current_arrears" +
            " FROM uma_customer customer" +
            " where 1=1 and del_flag = 0 and id = :id",nativeQuery = true)
    Customer findByIdWithArrearsList(@Param("id") Integer id, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT customer.id,customer.NAME,customer.CODE,customer.address,customer.contacts,customer.contact_phone,customer.remark,customer.create_date,customer.create_user,customer.del_flag,customer.full_name,customer.account,customer.over_arrears,customer.reconciliation," +
            " (SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.delivery_date < :startTime) as total_arrears" +
            ",(SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.delivery_date BETWEEN :startTime and :endTime) as current_arrears" +
            ",(SELECT ifnull(sum( remainder ),0) FROM uma_chemical_fiber_delivery_note n WHERE n.note_status between 4 and 6 and n.enable = 1 and  n.customer_id = customer.id and n.delivery_date BETWEEN :startTime and :endTime) as remainder" +
            " FROM uma_customer customer" +
            " where 1=1 and del_flag = 0 and id = :id",nativeQuery = true)
    Map<String, Object> findByIdWithArrearsMap(@Param("id") Integer id, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT COUNT(id) FROM uma_customer",nativeQuery = true)
    Integer findSize();
}