package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ScanRecordLabel;
import me.zhengjie.uma_mes.service.ScanRecordLabelService;
import me.zhengjie.uma_mes.service.dto.ScanRecordLabelQueryCriteria;
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
* @date 2019-11-21
*/
@Api(tags = "ScanRecordLabel管理")
@RestController
@RequestMapping("/api/scanRecordLabel")
public class ScanRecordLabelController {

    private final ScanRecordLabelService scanRecordLabelService;

    public ScanRecordLabelController(ScanRecordLabelService scanRecordLabelService) {
        this.scanRecordLabelService = scanRecordLabelService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('scanRecordLabel:list')")
    public void download(HttpServletResponse response, ScanRecordLabelQueryCriteria criteria) throws IOException {
        scanRecordLabelService.download(scanRecordLabelService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ScanRecordLabel")
    @ApiOperation("查询ScanRecordLabel")
    @PreAuthorize("@el.check('scanRecordLabel:list')")
    public ResponseEntity getScanRecordLabels(ScanRecordLabelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(scanRecordLabelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ScanRecordLabel")
    @ApiOperation("新增ScanRecordLabel")
    @PreAuthorize("@el.check('scanRecordLabel:add')")
    public ResponseEntity create(@Validated @RequestBody ScanRecordLabel resources){
        return new ResponseEntity<>(scanRecordLabelService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ScanRecordLabel")
    @ApiOperation("修改ScanRecordLabel")
    @PreAuthorize("@el.check('scanRecordLabel:edit')")
    public ResponseEntity update(@Validated @RequestBody ScanRecordLabel resources){
        scanRecordLabelService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ScanRecordLabel")
    @ApiOperation("删除ScanRecordLabel")
    @PreAuthorize("@el.check('scanRecordLabel:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        scanRecordLabelService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}