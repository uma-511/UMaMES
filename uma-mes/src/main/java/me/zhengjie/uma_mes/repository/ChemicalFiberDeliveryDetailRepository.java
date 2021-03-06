package me.zhengjie.uma_mes.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryDetailRepository extends JpaRepository<ChemicalFiberDeliveryDetail, Integer>, JpaSpecificationExecutor<ChemicalFiberDeliveryDetail> {

    @Query(value = "SELECT * FROM uma_chemical_fiber_delivery_detail where scan_number = :id",nativeQuery = true)
    List<ChemicalFiberDeliveryDetail> getDetailList(@Param("id") String id);

    @Query(value = "SELECT * FROM uma_chemical_fiber_delivery_detail a join uma_chemical_fiber_delivery_note b on a.scan_number = b.scan_number where b.customer_code = :code and b.delivery_date between :start and :end",nativeQuery = true)
    List<ChemicalFiberDeliveryDetail> getDetailListData(@Param("code") String code, @Param("start") String start, @Param("end") String end );

    @Query(value = "select b.scan_number, b.delivery_date, b.customer_name, a.prod_name, a.unit, a.total_price, a.total_number, a.real_quantity, a.real_price   from uma_chemical_fiber_delivery_detail a\n" +
            "join uma_chemical_fiber_delivery_note b on a.scan_number = b.scan_number where  b.delivery_date between ?1 and ?2 and b.note_status >= 3 and b.invalid = 0 and b.customer_name like %?3% and a.prod_name like %?4% and a.scan_number like %?5%",
            countQuery = "select count(*) as totalElements from uma_chemical_fiber_delivery_detail a\n" +
            "join uma_chemical_fiber_delivery_note b on a.scan_number = b.scan_number where  b.delivery_date between ?1 and ?2 and b.note_status >= 3 and b.invalid = 0 and b.customer_name like %?3% and a.prod_name like %?4% and a.scan_number like %?5%"
            ,nativeQuery = true)
    Page<Map<String, Object>> getSalesList(String date1, String date2, String user, String prod, String scanNumber, Pageable tempPageable);

    @Query(value = "select b.scan_number, b.delivery_date, b.customer_name, a.prod_name, a.unit, a.total_price, a.total_number, a.real_quantity, a.real_price   from uma_chemical_fiber_delivery_detail a\n" +
            "join uma_chemical_fiber_delivery_note b on a.scan_number = b.scan_number where  b.delivery_date between ?1 and ?2 and b.note_status >= 3 and b.invalid = 0 and b.customer_name like %?3% and a.prod_name like %?4% and a.scan_number like %?5%",
            nativeQuery = true)
    List<Map<String, Object>> getSales(String date1, String date2, String User, String prod, String scanNumber);

    @Query(value = "SELECT sum(total_price) FROM `uma_chemical_fiber_delivery_detail` where scan_number = ?1",
            nativeQuery = true)
    BigDecimal getTotal(String scanNumber);

    @Query(value = "SELECT d.selling_price FROM uma_chemical_fiber_delivery_detail d " +
            "where d.scan_number in (SELECT n.scan_number from uma_chemical_fiber_delivery_note n where n.customer_id = :customerId) " +
            "and d.selling_price is not null and d.prod_model=:prodModel ORDER BY id DESC LIMIT 1",nativeQuery = true)
    String getLastSellingPrice(Integer customerId,String prodModel);



}