package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.AcidPersionPerformance;
import me.zhengjie.uma_mes.service.AcidPersionPerformanceService;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceQueryCriteria;
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
* @date 2020-09-05
*/
@Api(tags = "AcidPersionPerformance管理")
@RestController
@RequestMapping("/api/acidPersionPerformance")
public class AcidPersionPerformanceController {

    private final AcidPersionPerformanceService acidPersionPerformanceService;

    public AcidPersionPerformanceController(AcidPersionPerformanceService acidPersionPerformanceService) {
        this.acidPersionPerformanceService = acidPersionPerformanceService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, AcidPersionPerformanceQueryCriteria criteria) throws IOException {
        // acidPersionPerformanceService.download(acidPersionPerformanceService.queryAll(criteria), response);
        if(null != criteria.getMonthTime() && !criteria.getMonthTime().equals("")){
            Date date = new Date(criteria.getMonthTime());
            try{
                criteria.setStartTime(changeToStartTime(date));
                criteria.setEndTime(changeToEndTime(date));
            }catch (Exception e){
                throw new BadRequestException("日期条件转换异常");
            }
        }
        acidPersionPerformanceService.downloadAcidPersonPerformance(criteria, response);
    }

    @GetMapping
    @Log("查询AcidPersionPerformance")
    @ApiOperation("查询AcidPersionPerformance")
    public ResponseEntity getAcidPersionPerformances(AcidPersionPerformanceQueryCriteria criteria, Pageable pageable){
        if(null != criteria.getMonthTime() && !criteria.getMonthTime().equals("")){
            Date date = new Date(criteria.getMonthTime());
            try{
                criteria.setStartTime(changeToStartTime(date));
                criteria.setEndTime(changeToEndTime(date));
            }catch (Exception e){
                throw new BadRequestException("日期条件转换异常");
            }
        }
        return new ResponseEntity<>(acidPersionPerformanceService.queryAll(criteria,pageable),HttpStatus.OK);
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
    @Log("新增AcidPersionPerformance")
    @ApiOperation("新增AcidPersionPerformance")
    public ResponseEntity create(@Validated @RequestBody AcidPersionPerformance resources){
        return new ResponseEntity<>(acidPersionPerformanceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改AcidPersionPerformance")
    @ApiOperation("修改AcidPersionPerformance")
    public ResponseEntity update(@Validated @RequestBody AcidPersionPerformance resources){
        acidPersionPerformanceService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除AcidPersionPerformance")
    @ApiOperation("删除AcidPersionPerformance")
    public ResponseEntity delete(@PathVariable Integer id){
        acidPersionPerformanceService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}