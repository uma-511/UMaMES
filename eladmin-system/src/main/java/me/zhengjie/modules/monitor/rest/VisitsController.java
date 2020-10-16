package me.zhengjie.modules.monitor.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.modules.monitor.service.VisitsService;
import me.zhengjie.uma_mes.service.dto.VisitsQueryCrteria;
import me.zhengjie.utils.RequestHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-13
 */
@RestController
@RequestMapping("/api/visits")
@Api(tags = "系统:访问记录管理")
public class VisitsController {

    private final VisitsService visitsService;

    public VisitsController(VisitsService visitsService) {
        this.visitsService = visitsService;
    }

    @PostMapping
    @ApiOperation("创建访问记录")
    public ResponseEntity create(){
        visitsService.count(RequestHolder.getHttpServletRequest());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(value = "/get")
    @ApiOperation("查询")
    public ResponseEntity get(@Validated @RequestBody VisitsQueryCrteria crteria){
        return new ResponseEntity<>(visitsService.get(crteria),HttpStatus.OK);
    }

    @GetMapping(value = "/chartData")
    @ApiOperation("查询图表数据")
    public ResponseEntity getChartData(){
        return new ResponseEntity<>(visitsService.getChartData(),HttpStatus.OK);
    }
}
