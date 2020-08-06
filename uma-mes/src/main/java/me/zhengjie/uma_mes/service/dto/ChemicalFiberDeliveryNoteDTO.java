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
public class ChemicalFiberDeliveryNoteDTO implements Serializable {

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

    // 车牌号
    private String carNumber;

    // 交付日期
    private Timestamp deliveryDate;

    // 主司机
    private String driverMain;

    // 副司机
    private String driverDeputy;

    // 订单状态：0-销毁 1-待打印 2-待出库 3-待签收 4-回填 5-待结款 6-完结
    private Integer noteStatus;

    // 装卸员1
    private String loaderOne;

    // 装卸员2
    private String loaderTwo;

    // 当前欠款
    private BigDecimal balance;

    // 付款方式
    private String payment;

    //失效状态
    private Integer invalid;

    //失效状态
    private Integer backNoteStatus;
}