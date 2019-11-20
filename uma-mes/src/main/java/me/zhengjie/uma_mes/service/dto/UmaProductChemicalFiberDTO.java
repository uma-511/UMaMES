package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaProductChemicalFiberDTO implements Serializable {

    private Integer id;

    // 产品型号
    private String model;

    // 产品名称
    private String name;

    // 色号
    private String color;

    // 纤度
    private String fineness;

    // 创建日期
    private Timestamp createDate;

    // 创建人
    private String createUser;

    // 删除标识
    private Integer delFlag;
}