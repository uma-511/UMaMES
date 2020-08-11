package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNotePayDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @author Xie Ji Biao
* @date 2020-08-08
*/
@Api(tags = "ChemicalFiberDeliveryNotePayDetail管理")
@RestController
@RequestMapping("/api/chemicalFiberDeliveryNotePayDetail")
public class ChemicalFiberDeliveryNotePayDetailController {

    private final ChemicalFiberDeliveryNotePayDetailService chemicalFiberDeliveryNotePayDetailService;

    public ChemicalFiberDeliveryNotePayDetailController(ChemicalFiberDeliveryNotePayDetailService chemicalFiberDeliveryNotePayDetailService) {
        this.chemicalFiberDeliveryNotePayDetailService = chemicalFiberDeliveryNotePayDetailService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:list')")
    public void download(HttpServletResponse response, ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria) throws IOException {
        chemicalFiberDeliveryNotePayDetailService.download(chemicalFiberDeliveryNotePayDetailService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberDeliveryNotePayDetail")
    @ApiOperation("查询ChemicalFiberDeliveryNotePayDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:list')")
    public ResponseEntity getChemicalFiberDeliveryNotePayDetails(ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberDeliveryNotePayDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/getPayDetailList")
    @Log("getPayDetailList")
    @ApiOperation("getPayDetailList")
    public ResponseEntity getPayDetailList(@Validated @RequestBody ChemicalFiberDeliveryNotePayDetail resources){
        return new ResponseEntity<>(chemicalFiberDeliveryNotePayDetailService.findListByScanNumber(resources.getScanNumber()),HttpStatus.OK);
    }

    /*@PostMapping
    @Log("新增ChemicalFiberDeliveryNotePayDetail")
    @ApiOperation("新增ChemicalFiberDeliveryNotePayDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberDeliveryNotePayDetail resources){
        return new ResponseEntity<>(chemicalFiberDeliveryNotePayDetailService.create(resources),HttpStatus.CREATED);
    }*/

    @PostMapping(value = "/doPay")
    @Log("结账操作")
    @ApiOperation("结账操作")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:doPay')")
    public ResponseEntity doPay(@Validated @RequestBody ChemicalFiberDeliveryNotePayDetail resources){
        return new ResponseEntity<>(chemicalFiberDeliveryNotePayDetailService.doPay(resources),HttpStatus.CREATED);
    }

    @PostMapping(value = "/finalPay")
    @Log("结账操作")
    @ApiOperation("结账操作")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:finalPay')")
    public ResponseEntity finalPay(@Validated @RequestBody ChemicalFiberDeliveryNotePayDetail resources){
        return new ResponseEntity<>(chemicalFiberDeliveryNotePayDetailService.finalPay(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberDeliveryNotePayDetail")
    @ApiOperation("修改ChemicalFiberDeliveryNotePayDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberDeliveryNotePayDetail resources){
        chemicalFiberDeliveryNotePayDetailService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberDeliveryNotePayDetail")
    @ApiOperation("删除ChemicalFiberDeliveryNotePayDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberDeliveryNotePayDetailService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}