package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.CycleLabel;
import me.zhengjie.uma_mes.service.CycleLabelService;
import me.zhengjie.uma_mes.service.dto.CycleLabelQueryCriteria;
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
* @date 2020-09-09
*/
@Api(tags = "CycleLabel管理")
@RestController
@RequestMapping("/api/cycleLabel")
public class CycleLabelController {

    private final CycleLabelService cycleLabelService;

    public CycleLabelController(CycleLabelService cycleLabelService) {
        this.cycleLabelService = cycleLabelService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('cycleLabel:list')")
    public void download(HttpServletResponse response, CycleLabelQueryCriteria criteria) throws IOException {
        cycleLabelService.download(cycleLabelService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询CycleLabel")
    @ApiOperation("查询CycleLabel")
    @PreAuthorize("@el.check('cycleLabel:list')")
    public ResponseEntity getCycleLabels(CycleLabelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(cycleLabelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增CycleLabel")
    @ApiOperation("新增CycleLabel")
    @PreAuthorize("@el.check('cycleLabel:add')")
    public ResponseEntity create(@Validated @RequestBody CycleLabel resources){
        return new ResponseEntity<>(cycleLabelService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改CycleLabel")
    @ApiOperation("修改CycleLabel")
    @PreAuthorize("@el.check('cycleLabel:edit')")
    public ResponseEntity update(@Validated @RequestBody CycleLabel resources){
        cycleLabelService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除CycleLabel")
    @ApiOperation("删除CycleLabel")
    @PreAuthorize("@el.check('cycleLabel:del')")
    public ResponseEntity delete(@PathVariable Long id){
        cycleLabelService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}