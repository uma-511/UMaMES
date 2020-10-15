package me.zhengjie.uma_mes.rest;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        // monthlyWageStatisticsService.download(monthlyWageStatisticsService.queryAll(criteria), response);
        if(null != criteria.getMonthTime() && !criteria.getMonthTime().equals("")){
            Date date = new Date(criteria.getMonthTime());
            try{
                criteria.setStartTime(changeToStartTime(date));
                criteria.setEndTime(changeToEndTime(date));
            }catch (Exception e){
                throw new BadRequestException("日期条件转换异常");
            }
        }
        monthlyWageStatisticsService.downloadMonthlyWageStatistics(criteria, response);
    }

    public Date changeToEndTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        date=sdf.parse(sdf.format(calendar.getTime()));
        return date;
    }



    @GetMapping(value = "/oneKeyDelete")
    @Log("一键删除上月工资")
    @ApiOperation("一键删除上月工资")
    public ResponseEntity oneKeyDelete(){
        monthlyWageStatisticsService.oneKeyDelete();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/oneKeyReset")
    @Log("一键重置上月工资")
    @ApiOperation("一键重置上月工资")
    public ResponseEntity oneKeyReset(){
        monthlyWageStatisticsService.oneKeyReset();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Date changeToStartTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        date=sdf.parse(sdf.format(calendar.getTime()));
        return date;
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