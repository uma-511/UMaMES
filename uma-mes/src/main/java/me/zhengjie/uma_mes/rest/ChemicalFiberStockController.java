package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.service.ChemicalFiberStockService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockQueryCriteria;
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
@Api(tags = "优码MES:化纤-库存管理")
@RestController
@RequestMapping("/api/chemicalFiberStock")
public class ChemicalFiberStockController {

    private final ChemicalFiberStockService chemicalFiberStockService;

    public ChemicalFiberStockController(ChemicalFiberStockService chemicalFiberStockService) {
        this.chemicalFiberStockService = chemicalFiberStockService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberStock:list')")
    public void download(HttpServletResponse response, ChemicalFiberStockQueryCriteria criteria) throws IOException {
        chemicalFiberStockService.download(chemicalFiberStockService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberStock")
    @ApiOperation("查询ChemicalFiberStock")
    @PreAuthorize("@el.check('chemicalFiberStock:list')")
    public ResponseEntity getChemicalFiberStocks(ChemicalFiberStockQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberStockService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberStock")
    @ApiOperation("新增ChemicalFiberStock")
    @PreAuthorize("@el.check('chemicalFiberStock:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberStock resources){
        return new ResponseEntity<>(chemicalFiberStockService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberStock")
    @ApiOperation("修改ChemicalFiberStock")
    @PreAuthorize("@el.check('chemicalFiberStock:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberStock resources){
        chemicalFiberStockService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberStock")
    @ApiOperation("删除ChemicalFiberStock")
    @PreAuthorize("@el.check('chemicalFiberStock:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberStockService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}