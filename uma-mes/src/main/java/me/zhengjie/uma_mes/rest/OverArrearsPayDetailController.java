package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.domain.OverArrearsPayDetail;
import me.zhengjie.uma_mes.service.OverArrearsPayDetailService;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
import me.zhengjie.uma_mes.service.dto.OverArrearsPayDetailQueryCriteria;
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
* @date 2020-09-24
*/
@Api(tags = "OverArrearsPayDetail管理")
@RestController
@RequestMapping("/api/overArrearsPayDetail")
public class OverArrearsPayDetailController {

    private final OverArrearsPayDetailService overArrearsPayDetailService;

    public OverArrearsPayDetailController(OverArrearsPayDetailService overArrearsPayDetailService) {
        this.overArrearsPayDetailService = overArrearsPayDetailService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('overArrearsPayDetail:list')")
    public void download(HttpServletResponse response, OverArrearsPayDetailQueryCriteria criteria) throws IOException {
        overArrearsPayDetailService.download(overArrearsPayDetailService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询OverArrearsPayDetail")
    @ApiOperation("查询OverArrearsPayDetail")
    @PreAuthorize("@el.check('overArrearsPayDetail:list')")
    public ResponseEntity getOverArrearsPayDetails(CustomerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(overArrearsPayDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增OverArrearsPayDetail")
    @ApiOperation("新增OverArrearsPayDetail")
    @PreAuthorize("@el.check('overArrearsPayDetail:add')")
    public ResponseEntity create(@Validated @RequestBody OverArrearsPayDetail resources){
        return new ResponseEntity<>(overArrearsPayDetailService.create(resources),HttpStatus.CREATED);
    }

    @PostMapping(value = "/getPayDetailList")
    @Log("getPayDetailList")
    @ApiOperation("getPayDetailList")
    public ResponseEntity getPayDetailList(@Validated @RequestBody OverArrearsPayDetail resources){
        return new ResponseEntity<>(overArrearsPayDetailService.findListByCustomerId(resources.getCustomerId()),HttpStatus.OK);
    }

    @PutMapping
    @Log("修改OverArrearsPayDetail")
    @ApiOperation("修改OverArrearsPayDetail")
    @PreAuthorize("@el.check('overArrearsPayDetail:edit')")
    public ResponseEntity update(@Validated @RequestBody OverArrearsPayDetail resources){
        overArrearsPayDetailService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除OverArrearsPayDetail")
    @ApiOperation("删除OverArrearsPayDetail")
    @PreAuthorize("@el.check('overArrearsPayDetail:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        overArrearsPayDetailService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}