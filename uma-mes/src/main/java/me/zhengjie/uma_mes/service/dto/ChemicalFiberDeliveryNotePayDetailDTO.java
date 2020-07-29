package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-07-29
*/
@Data
public class ChemicalFiberDeliveryNotePayDetailDTO implements Serializable {

    private Integer id;

    // 客户id
    private Integer customerId;

    // 客户名称
    private String customerName;

    // 付款方式
    private String payment;

    // 创建日期
    private Timestamp createDate;

    // 支付日期
    private Timestamp payDate;

    // 操作人员
    private String inputUser;

    // 订单号
    private String scanNumber;
}