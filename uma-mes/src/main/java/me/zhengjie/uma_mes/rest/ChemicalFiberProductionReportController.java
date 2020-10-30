package me.zhengjie.uma_mes.rest;

import com.lgmn.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionReportService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionReportQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Tan Jun Ming
 * @date 2019-11-20
 */
@Api(tags = "优码MES:化纤-生产报表管理")
@RestController
@RequestMapping("/api/chemicalFiberProductionReport")
public class ChemicalFiberProductionReportController {

    @Autowired
    private ChemicalFiberProductionReportService chemicalFiberProductionReportService;

    @GetMapping
    @Log("查询chemicalFiberProductionReport")
    @ApiOperation("查询chemicalFiberProductionReport")
    //@PreAuthorize("@el.check('chemicalFiberProductionReport:list')")
    public ResponseEntity getChemicalFiberProductionReport(ChemicalFiberProductionReportQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberProductionReportService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/getDateil")
    @Log("查询生产报表详情")
    @ApiOperation("查询生产报表详情")
    //@PreAuthorize("@el.check('chemicalFiberProductionReport:list')")
    public ResponseEntity getChemicalFiberProductionReportDateil(ChemicalFiberLabelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberProductionReportService.queryAllDateil(criteria,pageable), HttpStatus.OK);
    }

    @PostMapping(value = "/getProductionReportSummaries")
    @Log("获取生产报表合计")
    @ApiOperation("获取生产报表合计")
    //@PreAuthorize("@el.check('chemicalFiberProduction:productionReport')")
    public Result getProductionReportSummaries(@RequestBody ChemicalFiberProductionReportQueryCriteria criteria) {
        return chemicalFiberProductionReportService.getProductionReportSummaries(criteria);
    }

    @Log("导出生产报表")
    @GetMapping(value = "/downloadProduct")
    // @PreAuthorize("@el.check('chemicalFiberProduction:list')")
    public void downloadProduct(HttpServletResponse response, ChemicalFiberProductionReportQueryCriteria criteria, Pageable pageable) throws IOException {
        chemicalFiberProductionReportService.downloadProduct(criteria, pageable, response);
    }
}
