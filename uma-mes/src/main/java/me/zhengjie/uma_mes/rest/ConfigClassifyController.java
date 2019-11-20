package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ConfigClassify;
import me.zhengjie.uma_mes.service.ConfigClassifyService;
import me.zhengjie.uma_mes.service.dto.ConfigClassifyQueryCriteria;
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
@Api(tags = "优码MES:配置分类管理")
@RestController
@RequestMapping("/api/configClassify")
public class ConfigClassifyController {

    private final ConfigClassifyService configClassifyService;

    public ConfigClassifyController(ConfigClassifyService configClassifyService) {
        this.configClassifyService = configClassifyService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('configClassify:list')")
    public void download(HttpServletResponse response, ConfigClassifyQueryCriteria criteria) throws IOException {
        configClassifyService.download(configClassifyService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ConfigClassify")
    @ApiOperation("查询ConfigClassify")
    @PreAuthorize("@el.check('configClassify:list')")
    public ResponseEntity getConfigClassifys(ConfigClassifyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(configClassifyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ConfigClassify")
    @ApiOperation("新增ConfigClassify")
    @PreAuthorize("@el.check('configClassify:add')")
    public ResponseEntity create(@Validated @RequestBody ConfigClassify resources){
        return new ResponseEntity<>(configClassifyService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ConfigClassify")
    @ApiOperation("修改ConfigClassify")
    @PreAuthorize("@el.check('configClassify:edit')")
    public ResponseEntity update(@Validated @RequestBody ConfigClassify resources){
        configClassifyService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ConfigClassify")
    @ApiOperation("删除ConfigClassify")
    @PreAuthorize("@el.check('configClassify:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        configClassifyService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}