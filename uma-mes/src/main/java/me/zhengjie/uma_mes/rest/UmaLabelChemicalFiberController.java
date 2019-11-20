package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaLabelChemicalFiber;
import me.zhengjie.uma_mes.service.UmaLabelChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaLabelChemicalFiberQueryCriteria;
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
@Api(tags = "UmaLabelChemicalFiber管理")
@RestController
@RequestMapping("/api/umaLabelChemicalFiber")
public class UmaLabelChemicalFiberController {

    private final UmaLabelChemicalFiberService umaLabelChemicalFiberService;

    public UmaLabelChemicalFiberController(UmaLabelChemicalFiberService umaLabelChemicalFiberService) {
        this.umaLabelChemicalFiberService = umaLabelChemicalFiberService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaLabelChemicalFiber:list')")
    public void download(HttpServletResponse response, UmaLabelChemicalFiberQueryCriteria criteria) throws IOException {
        umaLabelChemicalFiberService.download(umaLabelChemicalFiberService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaLabelChemicalFiber")
    @ApiOperation("查询UmaLabelChemicalFiber")
    @PreAuthorize("@el.check('umaLabelChemicalFiber:list')")
    public ResponseEntity getUmaLabelChemicalFibers(UmaLabelChemicalFiberQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaLabelChemicalFiberService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaLabelChemicalFiber")
    @ApiOperation("新增UmaLabelChemicalFiber")
    @PreAuthorize("@el.check('umaLabelChemicalFiber:add')")
    public ResponseEntity create(@Validated @RequestBody UmaLabelChemicalFiber resources){
        return new ResponseEntity<>(umaLabelChemicalFiberService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaLabelChemicalFiber")
    @ApiOperation("修改UmaLabelChemicalFiber")
    @PreAuthorize("@el.check('umaLabelChemicalFiber:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaLabelChemicalFiber resources){
        umaLabelChemicalFiberService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaLabelChemicalFiber")
    @ApiOperation("删除UmaLabelChemicalFiber")
    @PreAuthorize("@el.check('umaLabelChemicalFiber:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaLabelChemicalFiberService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}