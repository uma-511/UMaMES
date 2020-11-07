package me.zhengjie.uma_mes.service.dto;


import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
@Data
public class ChemicalFiberCustomerOrderDTO implements Serializable {

    private Integer id;

    // 客户id
    private Integer customerId;

    // 订单编号
    private String customerNumber;

    // 客户名称
    private String customerName;

    // 客户编号
    private String code;

    // 客户地址
    private String address;

    // 联系人
    private String contacts;

    // 联系电话
    private String contactPhone;

    // 状态
    private Integer status;

    // 入库时间
    private Timestamp warehousingDate;

    // 删除标识
    private Integer delFlag;

    // 备注
    private String remark;

    // 客户单号
    private String contactNumber;

    // 传真
    private String fax;

    // 制单人
    private String createUser;

    // 送货时间
    private Timestamp deviceDate;
}
