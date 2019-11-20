package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaConfigClassify;
import me.zhengjie.uma_mes.service.UmaConfigClassifyService;
import me.zhengjie.uma_mes.service.dto.UmaConfigClassifyQueryCriteria;
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
@Api(tags = "UmaConfigClassify管理")
@RestController
@RequestMapping("/api/umaConfigClassify")
public class UmaConfigClassifyController {

    private final UmaConfigClassifyService umaConfigClassifyService;

    public UmaConfigClassifyController(UmaConfigClassifyService umaConfigClassifyService) {
        this.umaConfigClassifyService = umaConfigClassifyService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaConfigClassify:list')")
    public void download(HttpServletResponse response, UmaConfigClassifyQueryCriteria criteria) throws IOException {
        umaConfigClassifyService.download(umaConfigClassifyService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaConfigClassify")
    @ApiOperation("查询UmaConfigClassify")
    @PreAuthorize("@el.check('umaConfigClassify:list')")
    public ResponseEntity getUmaConfigClassifys(UmaConfigClassifyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaConfigClassifyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaConfigClassify")
    @ApiOperation("新增UmaConfigClassify")
    @PreAuthorize("@el.check('umaConfigClassify:add')")
    public ResponseEntity create(@Validated @RequestBody UmaConfigClassify resources){
        return new ResponseEntity<>(umaConfigClassifyService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaConfigClassify")
    @ApiOperation("修改UmaConfigClassify")
    @PreAuthorize("@el.check('umaConfigClassify:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaConfigClassify resources){
        umaConfigClassifyService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaConfigClassify")
    @ApiOperation("删除UmaConfigClassify")
    @PreAuthorize("@el.check('umaConfigClassify:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaConfigClassifyService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}