package me.zhengjie.uma_mes.rest;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.Receipt;
import me.zhengjie.uma_mes.service.ReceiptService;
import me.zhengjie.uma_mes.service.dto.ReceiptQueryCriteria;
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
@Api(tags = "Receipt管理")
@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('receipt:list')")
    public void download(HttpServletResponse response, ReceiptQueryCriteria criteria) throws IOException {
        receiptService.download(receiptService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Receipt")
    @ApiOperation("查询Receipt")
    @PreAuthorize("@el.check('receipt:list')")
    public ResponseEntity getReceipts(ReceiptQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(receiptService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Receipt")
    @ApiOperation("新增Receipt")
    @PreAuthorize("@el.check('receipt:add')")
    public ResponseEntity create(@Validated @RequestBody Receipt resources){
        return new ResponseEntity<>(receiptService.create(resources),HttpStatus.CREATED);
    }

    @Log("确认收款单")
    @ApiOperation("确认收款单")
    @GetMapping(value = "/doFinish/{id}")
    public ResponseEntity doFinish(@PathVariable Integer id) {
        receiptService.doFinish(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改Receipt")
    @ApiOperation("修改Receipt")
    @PreAuthorize("@el.check('receipt:edit')")
    public ResponseEntity update(@Validated @RequestBody Receipt resources){
        receiptService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Receipt")
    @ApiOperation("删除Receipt")
    @PreAuthorize("@el.check('receipt:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        receiptService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}