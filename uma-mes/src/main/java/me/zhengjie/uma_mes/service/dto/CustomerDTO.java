package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


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

    // 传真
    private String fax;

    // 删除标识
    private Integer delFlag;
}
