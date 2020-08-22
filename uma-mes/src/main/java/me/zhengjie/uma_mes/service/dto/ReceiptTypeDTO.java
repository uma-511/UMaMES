package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
@Data
public class ReceiptTypeDTO implements Serializable {

    private Integer id;

    // 收款类型
    private String name;

    // 创建日期
    private Timestamp createDate;

    // 是否可用
    private Boolean enable;

    // 创建用户
    private String createUser;
}