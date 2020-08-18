package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.Revenue;
import me.zhengjie.uma_mes.service.RevenueService;
import me.zhengjie.uma_mes.service.dto.RevenueQueryCriteria;
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
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Api(tags = "Revenue管理")
@RestController
@RequestMapping("/api/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('revenue:list')")
    public void download(HttpServletResponse response, RevenueQueryCriteria criteria) throws IOException {
        revenueService.download(revenueService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Revenue")
    @ApiOperation("查询Revenue")
    @PreAuthorize("@el.check('revenue:list')")
    public ResponseEntity getRevenues(RevenueQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(revenueService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Revenue")
    @ApiOperation("新增Revenue")
    @PreAuthorize("@el.check('revenue:add')")
    public ResponseEntity create(@Validated @RequestBody Revenue resources){
        return new ResponseEntity<>(revenueService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Revenue")
    @ApiOperation("修改Revenue")
    @PreAuthorize("@el.check('revenue:edit')")
    public ResponseEntity update(@Validated @RequestBody Revenue resources){
        revenueService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Revenue")
    @ApiOperation("删除Revenue")
    @PreAuthorize("@el.check('revenue:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        revenueService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}