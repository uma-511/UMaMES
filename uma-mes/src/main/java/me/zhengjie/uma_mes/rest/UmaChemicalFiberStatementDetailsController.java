package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatementDetails;
import me.zhengjie.uma_mes.service.UmaChemicalFiberStatementDetailsService;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDetailsQueryCriteria;
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
* @date 2020-03-11
*/
@Api(tags = "UmaChemicalFiberStatementDetails管理")
@RestController
@RequestMapping("/api/umaChemicalFiberStatementDetails")
public class UmaChemicalFiberStatementDetailsController {

    private final UmaChemicalFiberStatementDetailsService umaChemicalFiberStatementDetailsService;

    public UmaChemicalFiberStatementDetailsController(UmaChemicalFiberStatementDetailsService umaChemicalFiberStatementDetailsService) {
        this.umaChemicalFiberStatementDetailsService = umaChemicalFiberStatementDetailsService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaChemicalFiberStatementDetails:list')")
    public void download(HttpServletResponse response, UmaChemicalFiberStatementDetailsQueryCriteria criteria) throws IOException {
        umaChemicalFiberStatementDetailsService.download(umaChemicalFiberStatementDetailsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaChemicalFiberStatementDetails")
    @ApiOperation("查询UmaChemicalFiberStatementDetails")
    @PreAuthorize("@el.check('umaChemicalFiberStatementDetails:list')")
    public ResponseEntity getUmaChemicalFiberStatementDetailss(UmaChemicalFiberStatementDetailsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaChemicalFiberStatementDetailsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaChemicalFiberStatementDetails")
    @ApiOperation("新增UmaChemicalFiberStatementDetails")
    @PreAuthorize("@el.check('umaChemicalFiberStatementDetails:add')")
    public ResponseEntity create(@Validated @RequestBody UmaChemicalFiberStatementDetails resources){
        return new ResponseEntity<>(umaChemicalFiberStatementDetailsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaChemicalFiberStatementDetails")
    @ApiOperation("修改UmaChemicalFiberStatementDetails")
    @PreAuthorize("@el.check('umaChemicalFiberStatementDetails:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaChemicalFiberStatementDetails resources){
        umaChemicalFiberStatementDetailsService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaChemicalFiberStatementDetails")
    @ApiOperation("删除UmaChemicalFiberStatementDetails")
    @PreAuthorize("@el.check('umaChemicalFiberStatementDetails:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaChemicalFiberStatementDetailsService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}