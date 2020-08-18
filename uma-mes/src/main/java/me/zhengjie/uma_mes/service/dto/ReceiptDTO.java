package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Data
public class ReceiptDTO implements Serializable {

    private Integer id;

    // 流水号
    private String receiptNumber;

    // 客户名称
    private String customerName;

    // 客户ID
    private Integer customerId;

    // 收入类型
    private String type;

    // 收款账号
    private String recivedAccount;

    // 单据日期
    private Timestamp recivedDate;

    // 项目类型
    private String projectType;

    // 经办人
    private String operator;

    // 金额
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal amountOfMoney;

    // 单据编号
    private String recivedNumber;

    // 备注
    private String remark;

    // 创单人
    private String createUser;

    // 创单日期
    private Timestamp createDate;

    // 状态：0-失效 1-编辑 2-完结
    private Integer status;
}