package me.zhengjie.uma_mes.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChemicalFiberProductionSetProductionStatusDTO {
    private Integer productionId;
    @ApiModelProperty("0 暂停，1 继续，2 完成，3 取消")
    private Integer status;
}
