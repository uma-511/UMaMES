package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
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
@Api(tags = "ChemicalFiberProduction管理")
@RestController
@RequestMapping("/api/chemicalFiberProduction")
public class ChemicalFiberProductionController {

    private final ChemicalFiberProductionService chemicalFiberProductionService;

    public ChemicalFiberProductionController(ChemicalFiberProductionService chemicalFiberProductionService) {
        this.chemicalFiberProductionService = chemicalFiberProductionService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberProduction:list')")
    public void download(HttpServletResponse response, ChemicalFiberProductionQueryCriteria criteria) throws IOException {
        chemicalFiberProductionService.download(chemicalFiberProductionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberProduction")
    @ApiOperation("查询ChemicalFiberProduction")
    @PreAuthorize("@el.check('chemicalFiberProduction:list')")
    public ResponseEntity getChemicalFiberProductions(ChemicalFiberProductionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberProductionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberProduction")
    @ApiOperation("新增ChemicalFiberProduction")
    @PreAuthorize("@el.check('chemicalFiberProduction:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberProduction resources){
        return new ResponseEntity<>(chemicalFiberProductionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberProduction")
    @ApiOperation("修改ChemicalFiberProduction")
    @PreAuthorize("@el.check('chemicalFiberProduction:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberProduction resources){
        chemicalFiberProductionService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberProduction")
    @ApiOperation("删除ChemicalFiberProduction")
    @PreAuthorize("@el.check('chemicalFiberProduction:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberProductionService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}