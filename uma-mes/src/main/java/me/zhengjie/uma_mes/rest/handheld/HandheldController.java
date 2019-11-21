package me.zhengjie.uma_mes.rest.handheld;

import com.lgmn.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.dto.handheld.LabelMsgDto;
import me.zhengjie.uma_mes.service.dto.handheld.UploadDataDto;
import me.zhengjie.uma_mes.service.handheld.HandheldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "优码MES:手持机API")
@RestController
@RequestMapping("/api/handheld")
public class HandheldController {

    @Autowired
    HandheldService handheldService;

    @Log("获取标签信息")
    @ApiOperation("获取标签信息")
    @PostMapping(value = "/getLabelMsg")
    @AnonymousAccess()
    public Result getLabelMsg(@RequestBody LabelMsgDto labelMsgDto) {
        return handheldService.getLabelMsg(labelMsgDto);
    }

    @Log("入仓、出仓、退仓、退货")
    @ApiOperation("入仓、出仓、退仓、退货")
    @PostMapping(value = "/uploadData")
    @AnonymousAccess()
    public Result uploadData(@RequestBody UploadDataDto uploadDataDto) {
        return handheldService.uploadData(uploadDataDto);
    }



}
