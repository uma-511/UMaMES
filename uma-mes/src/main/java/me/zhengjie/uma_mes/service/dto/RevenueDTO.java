package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Data
public class RevenueDTO implements Serializable {

    private Integer id;

    // 收款分类
    private String recivedType;

    // 收款明细
    private String recivedDetail;

    // 收款金额
    private BigDecimal money;

    // 创建日期
    private Timestamp createDate;

    // 收款日期
    private Timestamp recivedDate;

    // 操作人员
    private String createUser;

    // 客户id
    private Integer customerId;

    // 客户名称
    private String customerName;
}