package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaScanRecord;
import me.zhengjie.uma_mes.service.UmaScanRecordService;
import me.zhengjie.uma_mes.service.dto.UmaScanRecordQueryCriteria;
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
@Api(tags = "UmaScanRecord管理")
@RestController
@RequestMapping("/api/umaScanRecord")
public class UmaScanRecordController {

    private final UmaScanRecordService umaScanRecordService;

    public UmaScanRecordController(UmaScanRecordService umaScanRecordService) {
        this.umaScanRecordService = umaScanRecordService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaScanRecord:list')")
    public void download(HttpServletResponse response, UmaScanRecordQueryCriteria criteria) throws IOException {
        umaScanRecordService.download(umaScanRecordService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaScanRecord")
    @ApiOperation("查询UmaScanRecord")
    @PreAuthorize("@el.check('umaScanRecord:list')")
    public ResponseEntity getUmaScanRecords(UmaScanRecordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaScanRecordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaScanRecord")
    @ApiOperation("新增UmaScanRecord")
    @PreAuthorize("@el.check('umaScanRecord:add')")
    public ResponseEntity create(@Validated @RequestBody UmaScanRecord resources){
        return new ResponseEntity<>(umaScanRecordService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaScanRecord")
    @ApiOperation("修改UmaScanRecord")
    @PreAuthorize("@el.check('umaScanRecord:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaScanRecord resources){
        umaScanRecordService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaScanRecord")
    @ApiOperation("删除UmaScanRecord")
    @PreAuthorize("@el.check('umaScanRecord:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaScanRecordService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}