package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.Equipment;
import me.zhengjie.uma_mes.service.EquipmentService;
import me.zhengjie.uma_mes.service.dto.EquipmentQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "优码MES:机台在线状态")
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @Log("查询机台在线状态查询")
    @ApiOperation("查询机台在线状态查询")
    @GetMapping
    //@PreAuthorize("@el.check('customer:list')")
    public ResponseEntity queryAll(EquipmentQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(equipmentService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @PutMapping
    @Log("修改机台名称")
    @ApiOperation("修改机台名称")
    //@PreAuthorize("@el.check('chemicalFiberLabel:edit')")
    public ResponseEntity update(@Validated @RequestBody Equipment resources){
        equipmentService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
