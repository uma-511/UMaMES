package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.BonusJobService;
import me.zhengjie.uma_mes.service.dto.BonusJobQueryCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Api(tags = "系统：岗位管理")
@RestController
@RequestMapping("/api/bonusJob")
public class BonusJobController {

    private final BonusJobService bonusJobService;

    public BonusJobController(BonusJobService bonusJobService) {
        this.bonusJobService = bonusJobService;
    }

    @Log("查询岗位")
    @ApiOperation("查询岗位")
    @GetMapping
    public ResponseEntity getJobs(BonusJobQueryCriteria criteria){
        return new ResponseEntity<>(bonusJobService.queryAll(criteria),HttpStatus.OK);
    }
}