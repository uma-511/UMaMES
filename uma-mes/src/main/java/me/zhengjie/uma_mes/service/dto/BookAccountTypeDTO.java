package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
@Data
public class BookAccountTypeDTO implements Serializable {

    private Integer id;

    // 记账项目
    private String name;

    // 创建日期
    private Timestamp createTime;

    // 是否可用
    private Boolean enable;

    //创建用户
    private  String createUser;
}