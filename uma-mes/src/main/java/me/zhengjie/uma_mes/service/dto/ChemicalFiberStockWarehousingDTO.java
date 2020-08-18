package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberStockWarehousingDTO implements Serializable {

    private Integer id;

    // 制单人
    private String createUser;

    // 总价格
    private BigDecimal totalPrice;

    // 入库单号
    private String scanNumber;

    // 制单时间
    private Timestamp createDate;

    // 入库日期
    private Timestamp warehousingDate;

    // 批号
    private Integer batchNumber;

    // 供应商名字
    private String supplierName;

    // 备注
    private String remark;

    // 状态码 0 待新增 1 待入库 2 入库成功
    private Integer warehousingStatus;


    // 主司机
    private String driverMain;

    // 副司机
    private String driverDeputy;

    // 副司机
    private String escortOne;

    // 副司机
    private String escortTwo;

    // 副司机
    private String carNumber;

    // 副司机
    private Integer invalid;

}
