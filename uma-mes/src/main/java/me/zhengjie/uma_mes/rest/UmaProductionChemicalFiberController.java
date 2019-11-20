package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaProductionChemicalFiber;
import me.zhengjie.uma_mes.service.UmaProductionChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaProductionChemicalFiberQueryCriteria;
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
@Api(tags = "UmaProductionChemicalFiber管理")
@RestController
@RequestMapping("/api/umaProductionChemicalFiber")
public class UmaProductionChemicalFiberController {

    private final UmaProductionChemicalFiberService umaProductionChemicalFiberService;

    public UmaProductionChemicalFiberController(UmaProductionChemicalFiberService umaProductionChemicalFiberService) {
        this.umaProductionChemicalFiberService = umaProductionChemicalFiberService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaProductionChemicalFiber:list')")
    public void download(HttpServletResponse response, UmaProductionChemicalFiberQueryCriteria criteria) throws IOException {
        umaProductionChemicalFiberService.download(umaProductionChemicalFiberService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaProductionChemicalFiber")
    @ApiOperation("查询UmaProductionChemicalFiber")
    @PreAuthorize("@el.check('umaProductionChemicalFiber:list')")
    public ResponseEntity getUmaProductionChemicalFibers(UmaProductionChemicalFiberQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaProductionChemicalFiberService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaProductionChemicalFiber")
    @ApiOperation("新增UmaProductionChemicalFiber")
    @PreAuthorize("@el.check('umaProductionChemicalFiber:add')")
    public ResponseEntity create(@Validated @RequestBody UmaProductionChemicalFiber resources){
        return new ResponseEntity<>(umaProductionChemicalFiberService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaProductionChemicalFiber")
    @ApiOperation("修改UmaProductionChemicalFiber")
    @PreAuthorize("@el.check('umaProductionChemicalFiber:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaProductionChemicalFiber resources){
        umaProductionChemicalFiberService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaProductionChemicalFiber")
    @ApiOperation("删除UmaProductionChemicalFiber")
    @PreAuthorize("@el.check('umaProductionChemicalFiber:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaProductionChemicalFiberService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}