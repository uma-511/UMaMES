package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabelInventory;
import me.zhengjie.uma_mes.service.ChemicalFiberLabelInventoryService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelInventoryQueryCriteria;
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
* @date 2020-04-14
*/
@Api(tags = "ChemicalFiberLabelInventory管理")
@RestController
@RequestMapping("/api/chemicalFiberLabelInventory")
public class ChemicalFiberLabelInventoryController {

    private final ChemicalFiberLabelInventoryService chemicalFiberLabelInventoryService;

    public ChemicalFiberLabelInventoryController(ChemicalFiberLabelInventoryService chemicalFiberLabelInventoryService) {
        this.chemicalFiberLabelInventoryService = chemicalFiberLabelInventoryService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberLabelInventory:list')")
    public void download(HttpServletResponse response, ChemicalFiberLabelInventoryQueryCriteria criteria) throws IOException {
        chemicalFiberLabelInventoryService.download(chemicalFiberLabelInventoryService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberLabelInventory")
    @ApiOperation("查询ChemicalFiberLabelInventory")
    @PreAuthorize("@el.check('chemicalFiberLabelInventory:list')")
    public ResponseEntity getChemicalFiberLabelInventorys(ChemicalFiberLabelInventoryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberLabelInventoryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberLabelInventory")
    @ApiOperation("新增ChemicalFiberLabelInventory")
    @PreAuthorize("@el.check('chemicalFiberLabelInventory:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberLabelInventory resources){
        return new ResponseEntity<>(chemicalFiberLabelInventoryService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberLabelInventory")
    @ApiOperation("修改ChemicalFiberLabelInventory")
    @PreAuthorize("@el.check('chemicalFiberLabelInventory:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberLabelInventory resources){
        chemicalFiberLabelInventoryService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberLabelInventory")
    @ApiOperation("删除ChemicalFiberLabelInventory")
    @PreAuthorize("@el.check('chemicalFiberLabelInventory:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberLabelInventoryService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}