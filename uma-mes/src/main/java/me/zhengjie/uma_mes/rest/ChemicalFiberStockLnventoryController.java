package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.ChemicalFiberStockLnventoryService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockLnventoryQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "优码MES:化纤-盘点单管理")
@RestController
@RequestMapping("/api/chemicalFiberStockLnventory")
public class ChemicalFiberStockLnventoryController {

    final ChemicalFiberStockLnventoryService chemicalFiberStockLnventoryService;

    public ChemicalFiberStockLnventoryController(ChemicalFiberStockLnventoryService chemicalFiberStockLnventoryService) {
        this.chemicalFiberStockLnventoryService = chemicalFiberStockLnventoryService;

    }

    @GetMapping
    @Log("查询ChemicalFiberStockLnventory")
    @ApiOperation("查询ChemicalFiberStockLnventory")
    //@PreAuthorize("@el.check('chemicalFiberStockLnventory:list')")
    public ResponseEntity queryAll(ChemicalFiberStockLnventoryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberStockLnventoryService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberStockLnventory")
    @ApiOperation("新增ChemicalFiberStockLnventory")
    //@PreAuthorize("@el.check('chemicalFiberStockLnventory:add')")
    public ResponseEntity create(){
        return new ResponseEntity<>(chemicalFiberStockLnventoryService.create(), HttpStatus.CREATED);
    }
}
