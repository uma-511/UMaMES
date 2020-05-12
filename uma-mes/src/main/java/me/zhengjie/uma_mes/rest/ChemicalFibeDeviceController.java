package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFibeDevice;
import me.zhengjie.uma_mes.service.ChemicalFibeDeviceService;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceQueryCriteria;
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
* @date 2020-04-16
*/
@Api(tags = "ChemicalFibeDevice管理")
@RestController
@RequestMapping("/api/chemicalFibeDevice")
public class ChemicalFibeDeviceController {

    private final ChemicalFibeDeviceService chemicalFibeDeviceService;

    public ChemicalFibeDeviceController(ChemicalFibeDeviceService chemicalFibeDeviceService) {
        this.chemicalFibeDeviceService = chemicalFibeDeviceService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFibeDevice:list')")
    public void download(HttpServletResponse response, ChemicalFibeDeviceQueryCriteria criteria) throws IOException {
        chemicalFibeDeviceService.download(chemicalFibeDeviceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFibeDevice")
    @ApiOperation("查询ChemicalFibeDevice")
    @PreAuthorize("@el.check('chemicalFibeDevice:list')")
    public ResponseEntity getChemicalFibeDevices(ChemicalFibeDeviceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFibeDeviceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFibeDevice")
    @ApiOperation("新增ChemicalFibeDevice")
    @PreAuthorize("@el.check('chemicalFibeDevice:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFibeDevice resources){
        return new ResponseEntity<>(chemicalFibeDeviceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFibeDevice")
    @ApiOperation("修改ChemicalFibeDevice")
    @PreAuthorize("@el.check('chemicalFibeDevice:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFibeDevice resources){
        chemicalFibeDeviceService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFibeDevice")
    @ApiOperation("删除ChemicalFibeDevice")
    @PreAuthorize("@el.check('chemicalFibeDevice:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFibeDeviceService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}