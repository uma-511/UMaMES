package me.zhengjie.uma_mes.rest;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.MonthlyWageStatistics;
import me.zhengjie.uma_mes.service.MonthlyWageStatisticsService;
import me.zhengjie.uma_mes.service.dto.MonthlyWageStatisticsQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-11
*/
@Api(tags = "MonthlyWageStatistics管理")
@RestController
@RequestMapping("/api/monthlyWageStatistics")
public class MonthlyWageStatisticsController {

    private final MonthlyWageStatisticsService monthlyWageStatisticsService;

    public MonthlyWageStatisticsController(MonthlyWageStatisticsService monthlyWageStatisticsService) {
        this.monthlyWageStatisticsService = monthlyWageStatisticsService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, MonthlyWageStatisticsQueryCriteria criteria) throws IOException {
        monthlyWageStatisticsService.download(monthlyWageStatisticsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询MonthlyWageStatistics")
    @ApiOperation("查询MonthlyWageStatistics")
    public ResponseEntity getMonthlyWageStatisticss(MonthlyWageStatisticsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(monthlyWageStatisticsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/generateWage")
    @Log("查询MonthlyWageStatistics")
    @ApiOperation("查询MonthlyWageStatistics")
    public ResponseEntity generateWage(){
        monthlyWageStatisticsService.generateWage();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("工资记录确认")
    @ApiOperation("工资记录确认")
    @GetMapping(value = "/doFinish/{id}")
    @AnonymousAccess()
    public ResponseEntity doFinish(@PathVariable Integer id) {
        monthlyWageStatisticsService.doFinish(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping
    @Log("新增MonthlyWageStatistics")
    @ApiOperation("新增MonthlyWageStatistics")
    public ResponseEntity create(@Validated @RequestBody MonthlyWageStatistics resources){
        return new ResponseEntity<>(monthlyWageStatisticsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改MonthlyWageStatistics")
    @ApiOperation("修改MonthlyWageStatistics")
    public ResponseEntity update(@Validated @RequestBody MonthlyWageStatistics resources){
        monthlyWageStatisticsService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除MonthlyWageStatistics")
    @ApiOperation("删除MonthlyWageStatistics")
    @PreAuthorize("@el.check('overArrearsPayDetail:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        monthlyWageStatisticsService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}