package me.zhengjie.uma_mes.service.dto.handheld;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LabelMsgDto {
    @ApiModelProperty("标签号")
    private String labelNumber;

    @ApiModelProperty("0：待入仓 1：入仓 2：出仓 3：作废 4：退仓 5：退货")
    private Integer status;
}
