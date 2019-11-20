package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaDeliveryDetailChemicalFiber;
import me.zhengjie.uma_mes.service.UmaDeliveryDetailChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryDetailChemicalFiberQueryCriteria;
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
@Api(tags = "UmaDeliveryDetailChemicalFiber管理")
@RestController
@RequestMapping("/api/umaDeliveryDetailChemicalFiber")
public class UmaDeliveryDetailChemicalFiberController {

    private final UmaDeliveryDetailChemicalFiberService umaDeliveryDetailChemicalFiberService;

    public UmaDeliveryDetailChemicalFiberController(UmaDeliveryDetailChemicalFiberService umaDeliveryDetailChemicalFiberService) {
        this.umaDeliveryDetailChemicalFiberService = umaDeliveryDetailChemicalFiberService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaDeliveryDetailChemicalFiber:list')")
    public void download(HttpServletResponse response, UmaDeliveryDetailChemicalFiberQueryCriteria criteria) throws IOException {
        umaDeliveryDetailChemicalFiberService.download(umaDeliveryDetailChemicalFiberService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaDeliveryDetailChemicalFiber")
    @ApiOperation("查询UmaDeliveryDetailChemicalFiber")
    @PreAuthorize("@el.check('umaDeliveryDetailChemicalFiber:list')")
    public ResponseEntity getUmaDeliveryDetailChemicalFibers(UmaDeliveryDetailChemicalFiberQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaDeliveryDetailChemicalFiberService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaDeliveryDetailChemicalFiber")
    @ApiOperation("新增UmaDeliveryDetailChemicalFiber")
    @PreAuthorize("@el.check('umaDeliveryDetailChemicalFiber:add')")
    public ResponseEntity create(@Validated @RequestBody UmaDeliveryDetailChemicalFiber resources){
        return new ResponseEntity<>(umaDeliveryDetailChemicalFiberService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaDeliveryDetailChemicalFiber")
    @ApiOperation("修改UmaDeliveryDetailChemicalFiber")
    @PreAuthorize("@el.check('umaDeliveryDetailChemicalFiber:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaDeliveryDetailChemicalFiber resources){
        umaDeliveryDetailChemicalFiberService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaDeliveryDetailChemicalFiber")
    @ApiOperation("删除UmaDeliveryDetailChemicalFiber")
    @PreAuthorize("@el.check('umaDeliveryDetailChemicalFiber:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaDeliveryDetailChemicalFiberService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}