package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

@Data
public class EquipmentDTO {

    private Integer id;

    // ip
    private String ip;

    // 状态
    private Integer status;

    private String name;
}
