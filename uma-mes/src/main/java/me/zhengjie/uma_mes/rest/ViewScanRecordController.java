 package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ViewScanRecord;
import me.zhengjie.uma_mes.service.ViewScanRecordService;
import me.zhengjie.uma_mes.service.dto.ViewScanRecordQueryCriteria;
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
* @author Xie Ji Biao
* @date 2019-11-26
*/
@Api(tags = "ViewScanRecord管理")
@RestController

@RequestMapping("/api/viewScanRecord")
public class ViewScanRecordController {

    private final ViewScanRecordService viewScanRecordService;

    public ViewScanRecordController(ViewScanRecordService viewScanRecordService) {
        this.viewScanRecordService = viewScanRecordService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('viewScanRecord:list')")
    public void download(HttpServletResponse response, ViewScanRecordQueryCriteria criteria) throws IOException {
        viewScanRecordService.download(viewScanRecordService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ViewScanRecord")
    @ApiOperation("查询ViewScanRecord")
    @PreAuthorize("@el.check('viewScanRecord:list')")
    public ResponseEntity getViewScanRecords(ViewScanRecordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(viewScanRecordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ViewScanRecord")
    @ApiOperation("新增ViewScanRecord")
    @PreAuthorize("@el.check('viewScanRecord:add')")
    public ResponseEntity create(@Validated @RequestBody ViewScanRecord resources){
        return new ResponseEntity<>(viewScanRecordService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ViewScanRecord")
    @ApiOperation("修改ViewScanRecord")
    @PreAuthorize("@el.check('viewScanRecord:edit')")
    public ResponseEntity update(@Validated @RequestBody ViewScanRecord resources){
        viewScanRecordService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/id")
    @Log("删除ViewScanRecord")
    @ApiOperation("删除ViewScanRecord")
    @PreAuthorize("@el.check('viewScanRecord:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        viewScanRecordService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}