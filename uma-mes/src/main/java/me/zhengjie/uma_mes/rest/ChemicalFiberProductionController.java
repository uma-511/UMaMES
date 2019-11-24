package me.zhengjie.uma_mes.rest;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionSetMachinesDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionSetProductionStatusDTO;
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
@Api(tags = "优码MES:化纤-生产单管理")
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

    @Log("设置机台号")
    @ApiOperation("设置机台号")
    @PostMapping(value = "/setMachines")
    @AnonymousAccess()
    public ResponseEntity setMachines(@RequestBody ChemicalFiberProductionSetMachinesDTO resources){
        return new ResponseEntity<>(chemicalFiberProductionService.setMachines(resources),HttpStatus.CREATED);
    }

    @Log("生产单状态 0：待产 1：生产中 2：暂停 3：取消 4：完成；生产单：暂停、完成、取消、继续")
    @ApiOperation("生产单状态 0：待产 1：生产中 2：暂停 3：取消 4：完成；生产单：暂停、完成、取消、继续")
    @PostMapping(value = "/setProductionStatus")
    @AnonymousAccess()
    public ResponseEntity setProductionStatus(@RequestBody ChemicalFiberProductionSetProductionStatusDTO resources){
        return new ResponseEntity<>(chemicalFiberProductionService.setProductionStatus(resources),HttpStatus.CREATED);
    }
}