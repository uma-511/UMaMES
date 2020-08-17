package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.service.ChemicalFiberStockWarehousingDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailQueryCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "优码MES:化纤-入库单管理")
@RestController
@RequestMapping("/api/chemicalFiberStockWarehousingDetail")
public class ChemicalFiberStockWarehousingDetailController {

    final ChemicalFiberStockWarehousingDetailService chemicalFiberStockWarehousingDetailService;

    public ChemicalFiberStockWarehousingDetailController(ChemicalFiberStockWarehousingDetailService chemicalFiberStockWarehousingDetailService) {
        this.chemicalFiberStockWarehousingDetailService = chemicalFiberStockWarehousingDetailService;

    }

    @PostMapping(value = "/chemicalFiberStockWarehousingDetailList")
    @Log("查询ChemicalFiberStockWarehousingDetail")
    @ApiOperation("查询ChemicalFiberStockWarehousingDetail")
    @PreAuthorize("@el.check('ChemicalFiberStockWarehousingDetail:list')")
    public ResponseEntity queryAll(@RequestBody ChemicalFiberStockWarehousingDetailQueryCriteria criteria){
        return new ResponseEntity<>(chemicalFiberStockWarehousingDetailService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberStockWarehousingDetail")
    @ApiOperation("新增ChemicalFiberStockWarehousingDetail")
    @PreAuthorize("@el.check('ChemicalFiberStockWarehousingDetail:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberStockWarehousingDetail resources){
        return new ResponseEntity<>(chemicalFiberStockWarehousingDetailService.create(resources), HttpStatus.CREATED);
    }


    @PutMapping
    @Log("修改ChemicalFiberStockWarehousingDetail")
    @ApiOperation("修改ChemicalFiberStockWarehousingDetail")
    @PreAuthorize("@el.check('ChemicalFiberStockWarehousingDetail:edit')")
    public ResponseEntity update(@Validated @RequestBody List<ChemicalFiberStockWarehousingDetail> resources){
        chemicalFiberStockWarehousingDetailService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberStockWarehousingDetail")
    @ApiOperation("删除ChemicalFiberStockWarehousingDetail")
    @PreAuthorize("@el.check('ChemicalFiberStockWarehousingDetail:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberStockWarehousingDetailService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
