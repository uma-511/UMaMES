package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Xie Ji Biao
* @date 2019-12-04
*/
@Data
public class HeartBeatDTO implements Serializable {

    private Integer id;

    // 终端id
    private String ip;

    // 终端端口
    private String port;

    // 心跳发送时间
    private Timestamp sendTime;

    // 心跳返回时间
    private Timestamp responseTime;

    // 创建时间
    private Timestamp createTime;
}