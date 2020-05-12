package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
@Data
public class ChemicalFibeDeviceDTO implements Serializable {

    private Integer id;

    // 设备ip
    private String ip;

    // 设备名称
    private String name;

    // 设备类型：0：打印机 1：触摸屏 2：扫描抢 3：秤 4：标签打印机 5：手持机
    private Integer type;

    // 使用状态：1：正常，0：停用
    private Integer status;

    // 设备分组id
    private Integer groupId;

    // 设备分组名称
    private String groupName;
}