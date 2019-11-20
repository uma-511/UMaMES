package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class UmaScanRecordDTO implements Serializable {

    private Integer id;

    // 单号
    private String scanNumber;

    private Integer labelId;

    // 扫描员
    private String scanUser;

    // 扫描时间
    private Timestamp scanTime;

    // 扫描类型
    private String type;
}