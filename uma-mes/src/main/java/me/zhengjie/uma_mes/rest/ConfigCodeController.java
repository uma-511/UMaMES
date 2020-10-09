package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ConfigCode;
import me.zhengjie.uma_mes.service.ConfigCodeService;
import me.zhengjie.uma_mes.service.dto.ConfigCodeQueryCriteria;
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
* @date 2020-10-09
*/
@Api(tags = "ConfigCode管理")
@RestController
@RequestMapping("/api/configCode")
public class ConfigCodeController {

    private final ConfigCodeService configCodeService;

    public ConfigCodeController(ConfigCodeService configCodeService) {
        this.configCodeService = configCodeService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('configCode:list')")
    public void download(HttpServletResponse response, ConfigCodeQueryCriteria criteria) throws IOException {
        configCodeService.download(configCodeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ConfigCode")
    @ApiOperation("查询ConfigCode")
    @PreAuthorize("@el.check('configCode:list')")
    public ResponseEntity getConfigCodes(ConfigCodeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(configCodeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/getCompanyName")
    @Log("查询getCompanyName")
    @ApiOperation("查询getCompanyName")
    public ResponseEntity getCompanyName(){
        return new ResponseEntity<>(configCodeService.getCompanyName(),HttpStatus.OK);
    }

    @GetMapping(value = "/getSerialCode")
    @Log("查询getSerialCode")
    @ApiOperation("查询getSerialCode")
    public ResponseEntity getSerialCode(){
        return new ResponseEntity<>(configCodeService.getSerialCode(),HttpStatus.OK);
    }

    @GetMapping(value = "/getAddress")
    @Log("查询getAddress")
    @ApiOperation("查询getAddress")
    public ResponseEntity getAddress(){
        return new ResponseEntity<>(configCodeService.getAddress(),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ConfigCode")
    @ApiOperation("新增ConfigCode")
    @PreAuthorize("@el.check('configCode:add')")
    public ResponseEntity create(@Validated @RequestBody ConfigCode resources){
        return new ResponseEntity<>(configCodeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ConfigCode")
    @ApiOperation("修改ConfigCode")
    @PreAuthorize("@el.check('configCode:edit')")
    public ResponseEntity update(@Validated @RequestBody ConfigCode resources){
        configCodeService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ConfigCode")
    @ApiOperation("删除ConfigCode")
    @PreAuthorize("@el.check('configCode:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        configCodeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}