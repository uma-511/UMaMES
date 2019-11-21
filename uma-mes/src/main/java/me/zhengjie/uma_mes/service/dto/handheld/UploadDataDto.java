package me.zhengjie.uma_mes.service.dto.handheld;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UploadDataDto {
    @ApiModelProperty("Map 格式：{labelNumber: 2019110001, scanTime: 1574308820}")
    List<Map<String, Integer>> labelList;
    @ApiModelProperty("0：待入仓 1：入仓 2：出仓 3：作废 4：退仓 5：退货")
    private Integer status;
    @ApiModelProperty("扫描员")
    private String scanUser;
}
