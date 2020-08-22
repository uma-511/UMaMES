package me.zhengjie.uma_mes.rest;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.ChemicalFibeDashboardService;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDashboardDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDashboardQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Api(tags = "ChemicalFiberDeliveryNotePayDetail管理")
@RestController
@RequestMapping("/api/chemicalFibeDashboard")
public class ChemicalFibeDashboardController {


    @Autowired
    private ChemicalFibeDashboardService chemicalFibeDashboardService;

    @PostMapping
    @Log("查询chemicalFibeDashboard")
    @ApiOperation("查询chemicalFibeDashboard")
    //@PreAuthorize("@el.check('chemicalFiberDeliveryNotePayDetail:list')")
    public ResponseEntity queryAll(@RequestBody ChemicalFibeDashboardQueryCriteria criteria){
        return new ResponseEntity<>(chemicalFibeDashboardService.queryAll(criteria), HttpStatus.OK);
    }

}
