package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.service.ChemicalFiberStockWarehousingService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "优码MES:化纤-入库单管理")
@RestController
@RequestMapping("/api/chemicalFiberStockWarehousing")
public class ChemicalFiberStockWarehousingController {

    final ChemicalFiberStockWarehousingService chemicalFiberStockWarehousingService;

    public ChemicalFiberStockWarehousingController (ChemicalFiberStockWarehousingService chemicalFiberStockWarehousingService) {
        this.chemicalFiberStockWarehousingService = chemicalFiberStockWarehousingService;

    }

    @GetMapping
    @Log("查询ChemicalFiberStockWarehousing")
    @ApiOperation("查询ChemicalFiberStockWarehousing")
    @PreAuthorize("@el.check('chemicalFiberStockWarehousing:list')")
    public ResponseEntity queryAll(ChemicalFiberStockWarehousingQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberStockWarehousingService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberStockWarehousing")
    @ApiOperation("新增ChemicalFiberStockWarehousing")
    @PreAuthorize("@el.check('chemicalFiberStockWarehousing:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberStockWarehousing resources){
        return new ResponseEntity<>(chemicalFiberStockWarehousingService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberStockWarehousing")
    @ApiOperation("修改ChemicalFiberStockWarehousing")
    @PreAuthorize("@el.check('chemicalFiberStockWarehousing:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberStockWarehousing resources){
        chemicalFiberStockWarehousingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/warehousing")
    @Log("入库")
    @ApiOperation("入库")
    @PreAuthorize("@el.check('chemicalFiberStockWarehousing:edit')")
    public ResponseEntity warehousing(@Validated @RequestBody List<ChemicalFiberStockWarehousingDetail> resources){
        chemicalFiberStockWarehousingService.warehousing(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberStockWarehousing")
    @ApiOperation("删除ChemicalFiberStockWarehousing")
    @PreAuthorize("@el.check('ChemicalFiberStockWarehousing:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberStockWarehousingService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
