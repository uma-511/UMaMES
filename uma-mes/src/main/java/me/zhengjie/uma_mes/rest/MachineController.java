package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.Machine;
import me.zhengjie.uma_mes.service.MachineService;
import me.zhengjie.uma_mes.service.dto.MachineQueryCriteria;
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
* @date 2019-11-24
*/
@Api(tags = "Machine管理")
@RestController
@RequestMapping("/api/machine")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('machine:list')")
    public void download(HttpServletResponse response, MachineQueryCriteria criteria) throws IOException {
        machineService.download(machineService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Machine")
    @ApiOperation("查询Machine")
    @PreAuthorize("@el.check('machine:list')")
    public ResponseEntity getMachines(MachineQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(machineService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Machine")
    @ApiOperation("新增Machine")
    @PreAuthorize("@el.check('machine:add')")
    public ResponseEntity create(@Validated @RequestBody Machine resources){
        return new ResponseEntity<>(machineService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Machine")
    @ApiOperation("修改Machine")
    @PreAuthorize("@el.check('machine:edit')")
    public ResponseEntity update(@Validated @RequestBody Machine resources){
        machineService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Machine")
    @ApiOperation("删除Machine")
    @PreAuthorize("@el.check('machine:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        machineService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}