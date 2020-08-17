package me.zhengjie.uma_mes.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventory;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockLnventoryDetail;
import me.zhengjie.uma_mes.service.ChemicalFiberStockLnventoryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "优码MES:化纤-盘点单列表管理")
@RestController
@RequestMapping("/api/chemicalFiberStockLnventoryDetail")
public class ChemicalFiberStockLnventoryDetailController {

    final ChemicalFiberStockLnventoryDetailService chemicalFiberStockLnventoryDetailService;

    public ChemicalFiberStockLnventoryDetailController(ChemicalFiberStockLnventoryDetailService chemicalFiberStockLnventoryDetailService){
        this.chemicalFiberStockLnventoryDetailService = chemicalFiberStockLnventoryDetailService;
    }

    @GetMapping
    @Log("查询chemicalFiberStockLnventoryDetail")
    @ApiOperation("查询chemicalFiberStockLnventoryDetail")
    @PreAuthorize("@el.check('chemicalFiberStockLnventoryDetail:list')")
    public ResponseEntity queryAll(){
        return new ResponseEntity<>(chemicalFiberStockLnventoryDetailService.queryAll(), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增chemicalFiberStockLnventoryDetail")
    @ApiOperation("新增chemicalFiberStockLnventoryDetail")
    @PreAuthorize("@el.check('chemicalFiberStockLnventoryDetail:add')")
    public ResponseEntity create(@Validated @RequestBody List<ChemicalFiberStockLnventoryDetail> resources){

        return new ResponseEntity<>(chemicalFiberStockLnventoryDetailService.create(resources), HttpStatus.CREATED);
    }

    @PostMapping(value = "/List")
    @Log("查询chemicalFiberStockLnventoryDetailList")
    @ApiOperation("查询chemicalFiberStockLnventoryDetailList")
    @PreAuthorize("@el.check('chemicalFiberStockLnventoryDetail:List')")
    public ResponseEntity queryAllList(@Validated @RequestBody ChemicalFiberStockLnventory resources){

        return new ResponseEntity<>(chemicalFiberStockLnventoryDetailService.queryAllList(resources), HttpStatus.CREATED);
    }

    @PostMapping(value = "/addList")
    @Log("查询chemicalFiberStockLnventoryDetailList")
    @ApiOperation("查询chemicalFiberStockLnventoryDetailList")
    @PreAuthorize("@el.check('chemicalFiberStockLnventoryDetail:List')")
    public ResponseEntity update(@Validated @RequestBody List<ChemicalFiberStockLnventoryDetail> resources) {
        chemicalFiberStockLnventoryDetailService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
