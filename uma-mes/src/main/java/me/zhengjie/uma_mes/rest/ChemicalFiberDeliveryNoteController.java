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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Log("订单发货")
    @ApiOperation("订单发货")
    @GetMapping(value = "/sendOut/{id}")
    @AnonymousAccess()
    public ResponseEntity sendOut(@PathVariable Integer id) {
        chemicalFiberDeliveryNoteService.sendOut(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("订单签收")
    @ApiOperation("订单签收")
    @GetMapping(value = "/recived/{id}")
    @AnonymousAccess()
    public ResponseEntity recived(@PathVariable Integer id) {
        chemicalFiberDeliveryNoteService.recived(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("订单签收")
    @ApiOperation("订单签收")
    @GetMapping(value = "/reRecived/{id}")
    @AnonymousAccess()
    public ResponseEntity reRecived(@PathVariable Integer id) {
        chemicalFiberDeliveryNoteService.reRecived(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("订单失效")
    @ApiOperation("订单失效")
    @GetMapping(value = "/doInvalid/{id}")
    @AnonymousAccess()
    public ResponseEntity doInvalid(@PathVariable Integer id) {
        chemicalFiberDeliveryNoteService.doInvalid(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("订单恢复生效")
    @ApiOperation("订单恢复生效")
    @GetMapping(value = "/unInvalid/{id}")
    @AnonymousAccess()
    public ResponseEntity unInvalid(@PathVariable Integer id) {
        chemicalFiberDeliveryNoteService.unInvalid(id);
        return new ResponseEntity(HttpStatus.OK);
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

    @PostMapping("/getSummaryData")
    @Log("查询ChemicalFiberLabel")
    @ApiOperation("查询ChemicalFiberLabel")
    public Result getSummaryData(@RequestBody ChemicalFiberDeliveryNoteQueryCriteria criteria) {
        BigDecimal sumTotalCost = new BigDecimal(0);
        BigDecimal sumTotalPrice = new BigDecimal(0);
        BigDecimal sumRemainder = new BigDecimal(0);
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != criteria.getShowUnEnable() && criteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        criteria.setEnableList(booleanList);
        List<ChemicalFiberDeliveryNoteDTO> ChemicalFiberDeliveryNoteList = chemicalFiberDeliveryNoteService.queryAll(criteria);
        for (ChemicalFiberDeliveryNoteDTO Note : ChemicalFiberDeliveryNoteList) {
            if (Note.getTotalCost() != null) {
                sumTotalCost = sumTotalCost.add(Note.getTotalCost());
            }
            if (Note.getTotalPrice() != null) {
                sumTotalPrice = sumTotalPrice.add(Note.getTotalPrice());
            }
            if (Note.getRemainder() != null) {
                sumRemainder = sumRemainder.add(Note.getRemainder());
            }
        }
        //sumNetWeight =  sumNetWeight.multiply(new BigDecimal(1000));
        Map<String, Object> map = new HashMap<>();
        map.put("sumTotalCost", sumTotalCost);
        map.put("sumTotalPrice", sumTotalPrice);
        map.put("sumRemainder", sumRemainder);
        return Result.success(map);
    }
}