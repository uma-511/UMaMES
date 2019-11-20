package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaConfig;
import me.zhengjie.uma_mes.service.UmaConfigService;
import me.zhengjie.uma_mes.service.dto.UmaConfigQueryCriteria;
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
@Api(tags = "UmaConfig管理")
@RestController
@RequestMapping("/api/umaConfig")
public class UmaConfigController {

    private final UmaConfigService umaConfigService;

    public UmaConfigController(UmaConfigService umaConfigService) {
        this.umaConfigService = umaConfigService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaConfig:list')")
    public void download(HttpServletResponse response, UmaConfigQueryCriteria criteria) throws IOException {
        umaConfigService.download(umaConfigService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaConfig")
    @ApiOperation("查询UmaConfig")
    @PreAuthorize("@el.check('umaConfig:list')")
    public ResponseEntity getUmaConfigs(UmaConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaConfig")
    @ApiOperation("新增UmaConfig")
    @PreAuthorize("@el.check('umaConfig:add')")
    public ResponseEntity create(@Validated @RequestBody UmaConfig resources){
        return new ResponseEntity<>(umaConfigService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaConfig")
    @ApiOperation("修改UmaConfig")
    @PreAuthorize("@el.check('umaConfig:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaConfig resources){
        umaConfigService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaConfig")
    @ApiOperation("删除UmaConfig")
    @PreAuthorize("@el.check('umaConfig:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaConfigService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}