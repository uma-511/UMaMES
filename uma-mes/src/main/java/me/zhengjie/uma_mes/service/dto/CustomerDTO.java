package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.uma_mes.utils.CustomerBigDecimalSerialize;

import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class CustomerDTO implements Serializable {

    private Integer id;

    // 客户名称
    private String name;

    // 客户编号
    private String code;

    // 客户地址
    private String address;

    // 联系人
    private String contacts;

    // 联系电话
    private String contactPhone;

    // 备注
    private String remark;

    // 创建日期
    private Timestamp createDate;

    // 创建人
    private String createUser;

    // 客户全称
    private String fullName;

    // 客户账号金额
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal account;

    // 删除标识
    private Integer delFlag;

    // 客户当前总欠款
    private BigDecimal totalArrears;
}