package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-21
*/
@Data
public class ScanRecordLabelDTO implements Serializable {

    private Integer id;

    // 扫描记录id
    private Integer scanRecordId;

    // 标签id
    private Integer labelId;

    // 扫描时间
    private Timestamp scanTime;
}