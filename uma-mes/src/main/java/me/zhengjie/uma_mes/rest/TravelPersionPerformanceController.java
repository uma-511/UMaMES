package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.TravelPersionPerformance;
import me.zhengjie.uma_mes.service.TravelPersionPerformanceService;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceQueryCriteria;
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
* @date 2020-09-02
*/
@Api(tags = "TravelPersionPerformance管理")
@RestController
@RequestMapping("/api/travelPersionPerformance")
public class TravelPersionPerformanceController {

    private final TravelPersionPerformanceService travelPersionPerformanceService;

    public TravelPersionPerformanceController(TravelPersionPerformanceService travelPersionPerformanceService) {
        this.travelPersionPerformanceService = travelPersionPerformanceService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, TravelPersionPerformanceQueryCriteria criteria) throws IOException {
        // travelPersionPerformanceService.download(travelPersionPerformanceService.queryAll(criteria), response);
        if(null != criteria.getMonthTime() && !criteria.getMonthTime().equals("")){
            Date date = new Date(criteria.getMonthTime());
            try{
                criteria.setStartTime(changeToStartTime(date));
                criteria.setEndTime(changeToEndTime(date));
            }catch (Exception e){
                throw new BadRequestException("日期条件转换异常");
            }
        }
        travelPersionPerformanceService.downloadTravelPersonPerformance(criteria, response);
    }

    @GetMapping
    @Log("查询TravelPersionPerformance")
    @ApiOperation("查询TravelPersionPerformance")
    public ResponseEntity getTravelPersionPerformances(TravelPersionPerformanceQueryCriteria criteria, Pageable pageable){
        if(null != criteria.getMonthTime() && !criteria.getMonthTime().equals("")){
            Date date = new Date(criteria.getMonthTime());
            try{
                criteria.setStartTime(changeToStartTime(date));
                criteria.setEndTime(changeToEndTime(date));
            }catch (Exception e){
                throw new BadRequestException("日期条件转换异常");
            }
        }
        return new ResponseEntity<>(travelPersionPerformanceService.queryAll(criteria,pageable),HttpStatus.OK);
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

    public Date changeToStartTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        date=sdf.parse(sdf.format(calendar.getTime()));
        return date;
    }

    @PostMapping
    @Log("新增TravelPersionPerformance")
    @ApiOperation("新增TravelPersionPerformance")
    public ResponseEntity create(@Validated @RequestBody TravelPersionPerformance resources){
        return new ResponseEntity<>(travelPersionPerformanceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改TravelPersionPerformance")
    @ApiOperation("修改TravelPersionPerformance")
    public ResponseEntity update(@Validated @RequestBody TravelPersionPerformance resources){
        travelPersionPerformanceService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除TravelPersionPerformance")
    @ApiOperation("删除TravelPersionPerformance")
    public ResponseEntity delete(@PathVariable Integer id){
        travelPersionPerformanceService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}