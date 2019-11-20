package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.Config;
import me.zhengjie.uma_mes.service.ConfigService;
import me.zhengjie.uma_mes.service.dto.ConfigQueryCriteria;
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
@Api(tags = "Config管理")
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('config:list')")
    public void download(HttpServletResponse response, ConfigQueryCriteria criteria) throws IOException {
        configService.download(configService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Config")
    @ApiOperation("查询Config")
    @PreAuthorize("@el.check('config:list')")
    public ResponseEntity getConfigs(ConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(configService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Config")
    @ApiOperation("新增Config")
    @PreAuthorize("@el.check('config:add')")
    public ResponseEntity create(@Validated @RequestBody Config resources){
        return new ResponseEntity<>(configService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Config")
    @ApiOperation("修改Config")
    @PreAuthorize("@el.check('config:edit')")
    public ResponseEntity update(@Validated @RequestBody Config resources){
        configService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Config")
    @ApiOperation("删除Config")
    @PreAuthorize("@el.check('config:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        configService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}