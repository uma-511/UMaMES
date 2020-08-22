package me.zhengjie.uma_mes.rest;

import com.lgmn.common.result.Result;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;
import me.zhengjie.uma_mes.service.UmaChemicalFiberStatementService;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDetailsDTO;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementQueryCriteria;
import me.zhengjie.uma_mes.service.dto.statement.CreateStatementDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsAllListDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsListDto;
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
@Api(tags = "UmaChemicalFiberStatement管理")
@RestController
@RequestMapping("/api/umaChemicalFiberStatement")
public class UmaChemicalFiberStatementController {

    private final UmaChemicalFiberStatementService umaChemicalFiberStatementService;

    public UmaChemicalFiberStatementController(UmaChemicalFiberStatementService umaChemicalFiberStatementService) {
        this.umaChemicalFiberStatementService = umaChemicalFiberStatementService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaChemicalFiberStatement:list')")
    public void download(HttpServletResponse response, UmaChemicalFiberStatementQueryCriteria criteria) throws IOException {
        umaChemicalFiberStatementService.download(umaChemicalFiberStatementService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaChemicalFiberStatement")
    @ApiOperation("查询UmaChemicalFiberStatement")
    @PreAuthorize("@el.check('umaChemicalFiberStatement:list')")
    public ResponseEntity getUmaChemicalFiberStatements(UmaChemicalFiberStatementQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaChemicalFiberStatementService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaChemicalFiberStatement")
    @ApiOperation("新增UmaChemicalFiberStatement")
    @PreAuthorize("@el.check('umaChemicalFiberStatement:add')")
    public ResponseEntity create(@RequestBody CreateStatementDto createStatementDto){
        return new ResponseEntity<>(umaChemicalFiberStatementService.create1(createStatementDto),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaChemicalFiberStatement")
    @ApiOperation("修改UmaChemicalFiberStatement")
    @PreAuthorize("@el.check('umaChemicalFiberStatement:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaChemicalFiberStatement resources){
        umaChemicalFiberStatementService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaChemicalFiberStatement")
    @ApiOperation("删除UmaChemicalFiberStatement")
    @PreAuthorize("@el.check('umaChemicalFiberStatement:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaChemicalFiberStatementService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/getStatementDetailsList")
    @Log("获取对账单列表")
    @ApiOperation("获取对账单列表")
//    @PreAuthorize("@el.check('umaChemicalFiberStatement:add')")
    @AnonymousAccess()
    public ResponseEntity getStatementDetailsList(@RequestBody StatementDetailsListDto statementDetailsListDto){
        return new ResponseEntity<>(umaChemicalFiberStatementService.getStatementDetailsList(statementDetailsListDto),HttpStatus.OK);
    }

    @PostMapping("/getStatementDetailsAllList")
    @Log("获取对账单列表")
    @ApiOperation("获取对账单列表")
//    @PreAuthorize("@el.check('umaChemicalFiberStatement:add')")
    @AnonymousAccess()
    public ResponseEntity getStatementDetailsAllList(@RequestBody StatementDetailsAllListDto statementDetailsAllListDto){
        return new ResponseEntity<>(umaChemicalFiberStatementService.getStatementDetailsAllList(statementDetailsAllListDto),HttpStatus.OK);
    }

    @PostMapping("/getSums")
    @Log("获取对账单统计")
    @ApiOperation("获取对账单统计")
//    @PreAuthorize("@el.check('umaChemicalFiberStatement:add')")
    @AnonymousAccess()
    public Result getSums(@RequestBody StatementDetailsAllListDto statementDetailsAllListDto) {
        return umaChemicalFiberStatementService.getSums(statementDetailsAllListDto);
    }

    @Log("导出出库单")
    @ApiOperation("导出出库单")
    @GetMapping(value = "/exportStatement/{id}")
    @AnonymousAccess()
    public void exportStatement(HttpServletResponse response, @PathVariable Integer id) {
        umaChemicalFiberStatementService.exportStatement(response, id);
    }
}