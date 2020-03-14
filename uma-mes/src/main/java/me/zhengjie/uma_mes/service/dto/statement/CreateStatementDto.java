package me.zhengjie.uma_mes.service.dto.statement;

import lombok.Data;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;

@Data
public class CreateStatementDto {
    private CreateStatementDetailsDto createStatementDetailsDto;
    private UmaChemicalFiberStatement umaChemicalFiberStatement;
}
