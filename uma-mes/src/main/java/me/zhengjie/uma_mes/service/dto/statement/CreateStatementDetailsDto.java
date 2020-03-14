package me.zhengjie.uma_mes.service.dto.statement;

import lombok.Data;

@Data
public class CreateStatementDetailsDto {
    private Integer customerId;
    // 临时开始时间
    private Long tempStartTime;

    // 临时结束时间
    private Long tempEndTime;
}
