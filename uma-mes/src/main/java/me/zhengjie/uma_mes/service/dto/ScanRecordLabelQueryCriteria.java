package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author Tan Jun Ming
* @date 2019-11-21
*/
@Data
public class ScanRecordLabelQueryCriteria{

    // 扫描记录id
    @Query
    private Integer scanRecordId;
}