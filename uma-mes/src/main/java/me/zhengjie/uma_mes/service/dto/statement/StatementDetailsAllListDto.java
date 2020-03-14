package me.zhengjie.uma_mes.service.dto.statement;

import lombok.Data;

@Data
public class StatementDetailsAllListDto {
    private Integer statementId;

    private Integer pageNumber;

    private Integer pageSize;

    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;
}
