package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ScanRecord;
import me.zhengjie.uma_mes.service.ScanRecordService;
import me.zhengjie.uma_mes.service.dto.ScanRecordQueryCriteria;
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
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Api(tags = "ScanRecord管理")
@RestController
@RequestMapping("/api/scanRecord")
public class ScanRecordController {

    private final ScanRecordService scanRecordService;

    public ScanRecordController(ScanRecordService scanRecordService) {
        this.scanRecordService = scanRecordService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('scanRecord:list')")
    public void download(HttpServletResponse response, ScanRecordQueryCriteria criteria) throws IOException {
        scanRecordService.download(scanRecordService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ScanRecord")
    @ApiOperation("查询ScanRecord")
    @PreAuthorize("@el.check('scanRecord:list')")
    public ResponseEntity getScanRecords(ScanRecordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(scanRecordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ScanRecord")
    @ApiOperation("新增ScanRecord")
    @PreAuthorize("@el.check('scanRecord:add')")
    public ResponseEntity create(@Validated @RequestBody ScanRecord resources){
        return new ResponseEntity<>(scanRecordService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ScanRecord")
    @ApiOperation("修改ScanRecord")
    @PreAuthorize("@el.check('scanRecord:edit')")
    public ResponseEntity update(@Validated @RequestBody ScanRecord resources){
        scanRecordService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ScanRecord")
    @ApiOperation("删除ScanRecord")
    @PreAuthorize("@el.check('scanRecord:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        scanRecordService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}