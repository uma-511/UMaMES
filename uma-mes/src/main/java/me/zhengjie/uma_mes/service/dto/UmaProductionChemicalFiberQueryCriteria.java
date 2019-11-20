package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaProductionChemicalFiberQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String number;

    // 精确
    @Query
    private Integer prodId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodModel;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodColor;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String prodFineness;

    // 精确
    @Query
    private Integer customerId;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerName;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerContactPhone;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerContacts;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerAddress;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String customerCode;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp deliveryDate;

    // 精确
    @Query
    private String machineNumber;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private Timestamp createTime;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String createUser;
}