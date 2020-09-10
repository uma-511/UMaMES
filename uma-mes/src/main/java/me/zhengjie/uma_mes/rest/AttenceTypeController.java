package me.zhengjie.uma_mes.rest;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.AttenceType;
import me.zhengjie.uma_mes.service.AttenceTypeService;
import me.zhengjie.uma_mes.service.dto.AttenceTypeQueryCriteria;
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
* @author wave
* @date 2020-09-08
*/
@Api(tags = "AttenceType管理")
@RestController
@RequestMapping("/api/attenceType")
public class AttenceTypeController {

    private final AttenceTypeService attenceTypeService;

    public AttenceTypeController(AttenceTypeService attenceTypeService) {
        this.attenceTypeService = attenceTypeService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('attenceType:list')")
    public void download(HttpServletResponse response, AttenceTypeQueryCriteria criteria) throws IOException {
        attenceTypeService.download(attenceTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询AttenceType")
    @ApiOperation("查询AttenceType")
    //@PreAuthorize("@el.check('attenceType:list')")
    public ResponseEntity getAttenceTypes(AttenceTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(attenceTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询AttenceType")
    @ApiOperation("查询AttenceType")
    @PostMapping(value = "/getAttenceTypesList")
    public ResponseEntity getAttenceTypesList(AttenceTypeQueryCriteria criteria){
        return new ResponseEntity<>(attenceTypeService.queryAll(criteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增AttenceType")
    @ApiOperation("新增AttenceType")
    //@PreAuthorize("@el.check('attenceType:add')")
    public ResponseEntity create(@Validated @RequestBody AttenceType resources){
        return new ResponseEntity<>(attenceTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改AttenceType")
    @ApiOperation("修改AttenceType")
    //@PreAuthorize("@el.check('attenceType:edit')")
    public ResponseEntity update(@Validated @RequestBody AttenceType resources){
        attenceTypeService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除AttenceType")
    @ApiOperation("删除AttenceType")
    //@PreAuthorize("@el.check('attenceType:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        attenceTypeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}