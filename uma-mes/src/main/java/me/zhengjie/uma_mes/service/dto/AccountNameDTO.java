package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Data
public class AccountNameDTO implements Serializable {

    private Integer id;

    // 账户名称
    private String name;

    // 创建时间
    private Timestamp createTime;

    // 创建用户
    private String createUser;

    // 是否可用
    private Boolean enable;
}