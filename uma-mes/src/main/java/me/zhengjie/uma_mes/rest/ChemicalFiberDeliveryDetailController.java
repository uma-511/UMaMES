package me.zhengjie.uma_mes.rest;

import com.lgmn.common.result.Result;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryDetailQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNoteQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Api(tags = "优码MES:化纤-出货单明细管理")
@RestController
@RequestMapping("/api/chemicalFiberDeliveryDetail")
public class ChemicalFiberDeliveryDetailController {

    private final ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService;

    public ChemicalFiberDeliveryDetailController(ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService) {
        this.chemicalFiberDeliveryDetailService = chemicalFiberDeliveryDetailService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:list')")
    public void download(HttpServletResponse response, ChemicalFiberDeliveryDetailQueryCriteria criteria) throws IOException {
        chemicalFiberDeliveryDetailService.download(chemicalFiberDeliveryDetailService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberDeliveryDetail")
    @ApiOperation("查询ChemicalFiberDeliveryDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:list')")
    public ResponseEntity getChemicalFiberDeliveryDetails(ChemicalFiberDeliveryDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberDeliveryDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping("/getChemicalFiberDeliveryDetailsList")
    @Log("查询ChemicalFiberDeliveryDetail")
    @ApiOperation("查询ChemicalFiberDeliveryDetail")
    @AnonymousAccess()
//    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:list')")
    public ResponseEntity getChemicalFiberDeliveryDetailsList(@RequestBody ChemicalFiberDeliveryDetailQueryCriteria criteria){
        return new ResponseEntity<>(chemicalFiberDeliveryDetailService.queryAll(criteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberDeliveryDetail")
    @ApiOperation("新增ChemicalFiberDeliveryDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberDeliveryDetail resources){
        return new ResponseEntity<>(chemicalFiberDeliveryDetailService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberDeliveryDetail")
    @ApiOperation("修改ChemicalFiberDeliveryDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberDeliveryDetail resources){
        chemicalFiberDeliveryDetailService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberDeliveryDetail")
    @ApiOperation("删除ChemicalFiberDeliveryDetail")
    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberDeliveryDetailService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/getSalesReportSummaries")
    @Log("获取销售报表合计")
    @ApiOperation("获取销售报表合计")
    @AnonymousAccess
    public Result getSalesReportSummaries(@RequestBody ChemicalFiberDeliveryDetailQueryCriteria criteria) {
        return chemicalFiberDeliveryDetailService.getSalesReportSummaries(criteria);
    }

    @PutMapping(value = "/setChemicalFiberDeliveryDetailsList")
    @Log("新增多条列表数据")
    @ApiOperation("新增多条列表数据")
    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:edit')")
    public ResponseEntity createList(@Validated @RequestBody List<ChemicalFiberDeliveryDetail> resources) {
        chemicalFiberDeliveryDetailService.updateList(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}