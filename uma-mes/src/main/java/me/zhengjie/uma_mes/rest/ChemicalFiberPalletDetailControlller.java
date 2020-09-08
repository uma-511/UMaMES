package me.zhengjie.uma_mes.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import me.zhengjie.uma_mes.service.ChemicalFiberPalletDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDetailQueryCeiteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "托板记录详细管理")
@RestController
@RequestMapping("/api/chemicalFiberPalletDetail")
public class ChemicalFiberPalletDetailControlller {

    @Autowired
    private ChemicalFiberPalletDetailService chemicalFiberPalletDetailService;

    @PostMapping
    @Log("查询chemicalFiberPalletDetail")
    @ApiOperation("查询chemicalFiberPalletDetail")
    public ResponseEntity queryAll(@Validated @RequestBody ChemicalFiberPalletDetailQueryCeiteria resources){
        return new ResponseEntity<>(chemicalFiberPalletDetailService.queryAll(resources), HttpStatus.OK);
    }

}
