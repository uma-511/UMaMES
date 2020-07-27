package me.zhengjie.uma_mes.rest;

import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryDetailService;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Api(tags = "优码MES:化纤-出货单管理")
@RestController
@RequestMapping("/api/chemicalFiberDeliveryNote")
public class ChemicalFiberDeliveryNoteController {

    private final ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService;

    private final ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService;

    private final CustomerService customerService;

    public ChemicalFiberDeliveryNoteController(ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService,
                                               ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService,
                                               CustomerService customerService) {
        this.chemicalFiberDeliveryNoteService = chemicalFiberDeliveryNoteService;
        this.chemicalFiberDeliveryDetailService = chemicalFiberDeliveryDetailService;
        this.customerService = customerService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:list')")
    public void download(HttpServletResponse response, ChemicalFiberDeliveryNoteQueryCriteria criteria) throws IOException {
        chemicalFiberDeliveryNoteService.download(chemicalFiberDeliveryNoteService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberDeliveryNote")
    @ApiOperation("查询ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:list')")
    public ResponseEntity getChemicalFiberDeliveryNotes(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberDeliveryNoteService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberDeliveryNote")
    @ApiOperation("新增ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberDeliveryNote resources){
        return new ResponseEntity<>(chemicalFiberDeliveryNoteService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberDeliveryNote")
    @ApiOperation("修改ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberDeliveryNote resources){
        chemicalFiberDeliveryNoteService.update(resources);
        ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
        chemicalFiberDeliveryDetailQueryCriteria.setDeliveryNoteId(resources.getId());
        List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailList = chemicalFiberDeliveryDetailService.queryAll(chemicalFiberDeliveryDetailQueryCriteria);
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailList) {
            chemicalFiberDeliveryDetailDTO.setCustomerName(customerDTO.getName());
            ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail = new ChemicalFiberDeliveryDetail();
            ObjectTransfer.transValue(chemicalFiberDeliveryDetailDTO, chemicalFiberDeliveryDetail);
            chemicalFiberDeliveryDetailService.update(chemicalFiberDeliveryDetail);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberDeliveryNote")
    @ApiOperation("删除ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberDeliveryNoteService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("导出出库单")
    @ApiOperation("导出出库单")
    @GetMapping(value = "/downloadDeliveryNote/{id}")
    @AnonymousAccess()
    public void downloadDeliveryNote(HttpServletResponse response, @PathVariable Integer id) {
        chemicalFiberDeliveryNoteService.downloadDeliveryNote(id, response);
    }

    @Log("导出磅码单")
    @ApiOperation("导出磅码单")
    @PostMapping(value = "/exportPoundExcel")
    @AnonymousAccess()
    public void exportPoundExcel(HttpServletResponse response, @RequestBody ChemicalFiberDeliveryNoteExportPoundExcelDto chemicalFiberDeliveryNoteExportPoundExcelDto) {
        chemicalFiberDeliveryNoteService.exportPoundExcel(chemicalFiberDeliveryNoteExportPoundExcelDto, response);
    }

    @GetMapping(value = "/getSalesReport")
    @Log("获取销售报表")
    @ApiOperation("获取销售报表")
//    @PreAuthorize("@el.check('chemicalFiberDeliveryDetail:list')")
    @AnonymousAccess
    public ResponseEntity getSalesReport(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberDeliveryNoteService.getSalesReport(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/getSalesReportSummaries")
    @Log("获取销售报表合计")
    @ApiOperation("获取销售报表合计")
    @AnonymousAccess
    public Result getSalesReportSummaries(@RequestBody ChemicalFiberDeliveryNoteQueryCriteria criteria) {
        return chemicalFiberDeliveryNoteService.getSalesReportSummaries(criteria);
    }
}