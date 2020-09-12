package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import me.zhengjie.uma_mes.service.ChemicalFiberPalletService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletQueryCeiteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "托板记录管理")
@RestController
@RequestMapping("/api/chemicalFiberPallet")
public class ChemicalFiberPalletController {

    @Autowired
    private ChemicalFiberPalletService chemicalFiberPalletService;


    @GetMapping
    @Log("查询ChemicalFiberPallet")
    @ApiOperation("查询ChemicalFiberPallet")
    //@PreAuthorize("@el.check('chemicalFiberLabelInventory:list')")
    public ResponseEntity queryAll(ChemicalFiberPalletQueryCeiteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberPalletService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("导出托板单")
    @ApiOperation("导出托板单")
    @PostMapping(value = "/exportPoundExcel")
    @AnonymousAccess()
    public void exportPoundExcel(HttpServletResponse response, @RequestBody ChemicalFiberPallet chemicalFiberPallet) {
        chemicalFiberPalletService.exportPoundExcel(chemicalFiberPallet, response);
    }





}
