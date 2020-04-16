package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
@Data
public class ChemicalFibeDeviceGroupDTO implements Serializable {

    private Integer id;

    // 设备分组名称
    private String name;

    // 使用状态：0：停用 1：正常
    private Integer status;
}