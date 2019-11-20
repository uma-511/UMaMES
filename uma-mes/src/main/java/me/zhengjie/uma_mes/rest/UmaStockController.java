package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaStock;
import me.zhengjie.uma_mes.service.UmaStockService;
import me.zhengjie.uma_mes.service.dto.UmaStockQueryCriteria;
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
@Api(tags = "UmaStock管理")
@RestController
@RequestMapping("/api/umaStock")
public class UmaStockController {

    private final UmaStockService umaStockService;

    public UmaStockController(UmaStockService umaStockService) {
        this.umaStockService = umaStockService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaStock:list')")
    public void download(HttpServletResponse response, UmaStockQueryCriteria criteria) throws IOException {
        umaStockService.download(umaStockService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaStock")
    @ApiOperation("查询UmaStock")
    @PreAuthorize("@el.check('umaStock:list')")
    public ResponseEntity getUmaStocks(UmaStockQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaStockService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaStock")
    @ApiOperation("新增UmaStock")
    @PreAuthorize("@el.check('umaStock:add')")
    public ResponseEntity create(@Validated @RequestBody UmaStock resources){
        return new ResponseEntity<>(umaStockService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaStock")
    @ApiOperation("修改UmaStock")
    @PreAuthorize("@el.check('umaStock:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaStock resources){
        umaStockService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaStock")
    @ApiOperation("删除UmaStock")
    @PreAuthorize("@el.check('umaStock:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaStockService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}