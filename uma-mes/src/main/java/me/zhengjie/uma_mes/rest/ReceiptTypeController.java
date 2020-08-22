package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ReceiptType;
import me.zhengjie.uma_mes.service.ReceiptTypeService;
import me.zhengjie.uma_mes.service.dto.BookAccountTypeQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ReceiptTypeQueryCriteria;
import org.springframework.cache.annotation.Cacheable;
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
* @date 2020-08-15
*/
@Api(tags = "ReceiptType管理")
@RestController
@RequestMapping("/api/receiptType")
public class ReceiptTypeController {

    private final ReceiptTypeService receiptTypeService;

    public ReceiptTypeController(ReceiptTypeService receiptTypeService) {
        this.receiptTypeService = receiptTypeService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('receiptType:list')")
    public void download(HttpServletResponse response, ReceiptTypeQueryCriteria criteria) throws IOException {
        receiptTypeService.download(receiptTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ReceiptType")
    @ApiOperation("查询ReceiptType")
    @PreAuthorize("@el.check('receiptType:list')")
    public ResponseEntity getReceiptTypes(ReceiptTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(receiptTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/getReceiptTypeList")
    @Log("查询ReceiptType")
    @ApiOperation("查询ReceiptType")
    @PreAuthorize("@el.check('receiptType:list')")
    public ResponseEntity getReceiptTypeList(ReceiptTypeQueryCriteria criteria){
        return new ResponseEntity<>(receiptTypeService.queryAll(criteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ReceiptType")
    @ApiOperation("新增ReceiptType")
    @PreAuthorize("@el.check('receiptType:add')")
    public ResponseEntity create(@Validated @RequestBody ReceiptType resources){
        return new ResponseEntity<>(receiptTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ReceiptType")
    @ApiOperation("修改ReceiptType")
    @PreAuthorize("@el.check('receiptType:edit')")
    public ResponseEntity update(@Validated @RequestBody ReceiptType resources){
        receiptTypeService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ReceiptType")
    @ApiOperation("删除ReceiptType")
    @PreAuthorize("@el.check('receiptType:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        receiptTypeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}