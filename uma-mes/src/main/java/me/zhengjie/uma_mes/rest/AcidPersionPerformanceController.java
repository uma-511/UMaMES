package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
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
        acidPersionPerformanceService.download(acidPersionPerformanceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询AcidPersionPerformance")
    @ApiOperation("查询AcidPersionPerformance")
    public ResponseEntity getAcidPersionPerformances(AcidPersionPerformanceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(acidPersionPerformanceService.queryAll(criteria,pageable),HttpStatus.OK);
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