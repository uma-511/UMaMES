package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ErrorTextDTO {

    private Integer id;

    // 错误信息
    private String labelError;

    // 标签号码
    private String labelNumber;

    // 状态
    private Integer labelStatus;

    // 状态
    private Timestamp createDate;


}
