package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.BonusCycle;
import me.zhengjie.uma_mes.domain.BonusType;
import me.zhengjie.uma_mes.domain.CycleLabel;
import me.zhengjie.uma_mes.service.BonusTypeService;
import me.zhengjie.uma_mes.service.dto.BonusTypeQueryCriteria;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-09
*/
@Api(tags = "BonusType管理")
@RestController
@RequestMapping("/api/bonusType")
public class BonusTypeController {

    private final BonusTypeService bonusTypeService;

    public BonusTypeController(BonusTypeService bonusTypeService) {
        this.bonusTypeService = bonusTypeService;
    }


    @ApiOperation("获取单个bonusType")
    @GetMapping(value = "/{id}")
    public ResponseEntity getBonusType(@PathVariable Long id){
        return new ResponseEntity<>(bonusTypeService.findById(id), HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, BonusTypeQueryCriteria criteria) throws IOException {
        bonusTypeService.download(bonusTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询BonusType")
    @ApiOperation("查询BonusType")
    public ResponseEntity getBonusTypes(BonusTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(bonusTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增BonusType")
    @ApiOperation("新增BonusType")
    public ResponseEntity create(@Validated @RequestBody BonusType resources){
        return new ResponseEntity<>(bonusTypeService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改周期菜单")
    @ApiOperation("修改周期菜单")
    @PutMapping(value = "/cycle")
    public ResponseEntity updateCycle(@RequestBody BonusType resources){
        bonusTypeService.updateCycle(resources,bonusTypeService.findById(resources.getId()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改周期菜单")
    @ApiOperation("修改周期菜单")
    @PutMapping(value = "/job")
    public ResponseEntity updateJob(@RequestBody BonusType resources){
        bonusTypeService.updateJob(resources,bonusTypeService.findById(resources.getId()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("返回周期菜单")
    @GetMapping(value = "/getCycleMenusTree")
    public ResponseEntity getCycleMenusTree(){
        return new ResponseEntity<>(bonusTypeService.getCycleMenusTree(),HttpStatus.OK);
    }

    @ApiOperation("返回岗位菜单")
        @GetMapping(value = "/getBonusJobsTree")
    public ResponseEntity getBonusJobsTree(){
        return new ResponseEntity<>(bonusTypeService.getBonusJobsTree(),HttpStatus.OK);
    }

    @PutMapping
    @Log("修改BonusType")
    @ApiOperation("修改BonusType")
    public ResponseEntity update(@Validated @RequestBody BonusType resources){
        bonusTypeService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改周期菜单")
    @ApiOperation("修改周期菜单")
    @PutMapping(value = "/cycleMenu")
    public ResponseEntity updateCycleMenu(@RequestBody BonusType resources){
        bonusTypeService.updateCycleMenu(resources,bonusTypeService.findById(resources.getId()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除BonusType")
    @ApiOperation("删除BonusType")
    public ResponseEntity delete(@PathVariable Long id){
        bonusTypeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}