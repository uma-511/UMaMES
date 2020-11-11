package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
@Data
public class UmaChemicalFiberStatementDTO implements Serializable {

    // 主键
    private Integer id;

    // 对账单号
    private String accountCode;

    // 创建日期
    private Timestamp createDate;

    // 创建人
    private String createUser;

    // 客户ID
    private Integer customerId;

    // 客户名称
    private String customerName;

    // 客户联系人
    private String contacts;

    // 客户联系电话
    private String contactPhone;

    // 应收金额
    private BigDecimal receivable;

    // 上期欠款
    private BigDecimal accumulatedArrears;

    // 总欠金额
    private BigDecimal totalArrears;

    // 总欠金额
    private String fax;
}
