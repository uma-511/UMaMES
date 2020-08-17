package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberStockWarehousingQueryCriteria {


    @Query(type = Query.Type.INNER_LIKE)
    private Integer id;

    // 制单人
    @Query(type = Query.Type.INNER_LIKE)
    private String createUser;

    // 总价格
    @Query(type = Query.Type.INNER_LIKE)
    private BigDecimal totalPrice;

    // 入库单号
    @Query(type = Query.Type.INNER_LIKE)
    private String scanNumber;

    // 制单时间
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp createDate;

    // 入库日期
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp warehousingDate;

    // 批号
    @Query(type = Query.Type.INNER_LIKE)
    private Integer batchNumber;

    // 供应商名字
    @Query(type = Query.Type.INNER_LIKE)
    private String supplierName;

    // 主司机
    @Query(type = Query.Type.INNER_LIKE)
    private String driverMain;

    // 副司机
    @Query(type = Query.Type.INNER_LIKE)
    private String driverDeputy;

    // 副司机
    @Query(type = Query.Type.INNER_LIKE)
    private String escortOne;

    // 副司机
    @Query(type = Query.Type.INNER_LIKE)
    private String escortTwo;

    // 副司机
    @Query(type = Query.Type.INNER_LIKE)
    private String carNumber;

}
