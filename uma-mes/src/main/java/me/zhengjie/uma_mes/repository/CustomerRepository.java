package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.Customer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    @Query(value = "SELECT customer.id,customer.NAME,customer.CODE,customer.address,customer.contacts,customer.contact_phone,customer.remark,customer.create_date,customer.create_user,customer.del_flag,customer.full_name,customer.account," +
            " (SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE  n.customer_id = customer.id and n.create_date < :otherDate) as total_arrears" +
            ",(SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE  n.customer_id = customer.id and n.create_date like %:otherDate%) as current_arrears" +
            " FROM uma_customer customer" +
            " where 1=1 " +
            " and customer.NAME like %:name% and customer.CODE like %:code% and customer.address like %:address% and customer.contacts like %:contacts% and customer.contact_phone like %:contactPhone%" +
            " ORDER BY customer.id DESC LIMIT :Start,:End",nativeQuery = true)
    List<Customer> findAllWithTotalArrears(@Param("Start") Integer Start, @Param("End") Integer End, @Param("otherDate") String otherDate, @Param("name") String name, @Param("code") String code, @Param("address") String address, @Param("contacts") String contacts, @Param("contactPhone") String contactPhone);

    @Query(value = "SELECT customer.id,customer.NAME,customer.CODE,customer.address,customer.contacts,customer.contact_phone,customer.remark,customer.create_date,customer.create_user,customer.del_flag,customer.full_name,customer.account," +
            " (SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE  n.customer_id = customer.id and n.create_date < :otherDate) as total_arrears" +
            ",(SELECT ifnull(sum( balance ),0) FROM uma_chemical_fiber_delivery_note n WHERE  n.customer_id = customer.id and n.create_date like %:otherDate%) as current_arrears" +
            " FROM uma_customer customer" +
            " where 1=1 and id = :id",nativeQuery = true)
    Customer findByIdWithArrears(@Param("id") Integer id,@Param("otherDate") String otherDate);

    @Query(value = "SELECT COUNT(id) FROM uma_customer",nativeQuery = true)
    Integer findSize();
}