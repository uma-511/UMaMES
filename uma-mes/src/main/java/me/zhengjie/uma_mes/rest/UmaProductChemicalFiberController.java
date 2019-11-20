package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaProductChemicalFiber;
import me.zhengjie.uma_mes.service.UmaProductChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaProductChemicalFiberQueryCriteria;
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
@Api(tags = "UmaProductChemicalFiber管理")
@RestController
@RequestMapping("/api/umaProductChemicalFiber")
public class UmaProductChemicalFiberController {

    private final UmaProductChemicalFiberService umaProductChemicalFiberService;

    public UmaProductChemicalFiberController(UmaProductChemicalFiberService umaProductChemicalFiberService) {
        this.umaProductChemicalFiberService = umaProductChemicalFiberService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaProductChemicalFiber:list')")
    public void download(HttpServletResponse response, UmaProductChemicalFiberQueryCriteria criteria) throws IOException {
        umaProductChemicalFiberService.download(umaProductChemicalFiberService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaProductChemicalFiber")
    @ApiOperation("查询UmaProductChemicalFiber")
    @PreAuthorize("@el.check('umaProductChemicalFiber:list')")
    public ResponseEntity getUmaProductChemicalFibers(UmaProductChemicalFiberQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaProductChemicalFiberService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaProductChemicalFiber")
    @ApiOperation("新增UmaProductChemicalFiber")
    @PreAuthorize("@el.check('umaProductChemicalFiber:add')")
    public ResponseEntity create(@Validated @RequestBody UmaProductChemicalFiber resources){
        return new ResponseEntity<>(umaProductChemicalFiberService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaProductChemicalFiber")
    @ApiOperation("修改UmaProductChemicalFiber")
    @PreAuthorize("@el.check('umaProductChemicalFiber:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaProductChemicalFiber resources){
        umaProductChemicalFiberService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaProductChemicalFiber")
    @ApiOperation("删除UmaProductChemicalFiber")
    @PreAuthorize("@el.check('umaProductChemicalFiber:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaProductChemicalFiberService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}