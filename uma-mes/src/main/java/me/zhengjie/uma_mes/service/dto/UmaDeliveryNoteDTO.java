package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaDeliveryNoteDTO implements Serializable {

    private Integer id;

    // 出库单号
    private String scanNumber;

    // 客户id
    private Integer customerId;

    // 客户名称
    private String customerName;

    // 客户编号
    private String customerCode;

    // 客户地址
    private String customerAddress;

    // 联系人
    private String contacts;

    // 联系电话
    private String contactPhone;

    // 总成本
    private BigDecimal totalCost;

    // 总价
    private BigDecimal totalPrice;

    // 备注
    private String remark;

    // 业务员
    private String seller;

    // 仓管员
    private String storeKeeper;

    // 制单日期
    private Timestamp createDate;

    // 制单人
    private String createUser;
}