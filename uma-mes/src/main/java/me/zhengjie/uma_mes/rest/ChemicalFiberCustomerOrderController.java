package me.zhengjie.uma_mes.rest;


import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberCustomerOrder;
import me.zhengjie.uma_mes.service.ChemicalFiberCustomerOrderService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberCustomerOrderQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
@Api(tags = "客户订单管理")
@RestController
@RequestMapping("/api/chemicalFiberCustomerOrder")
public class ChemicalFiberCustomerOrderController {

    private final ChemicalFiberCustomerOrderService chemicalFiberCustomerOrderService;

    public ChemicalFiberCustomerOrderController(ChemicalFiberCustomerOrderService chemicalFiberCustomerOrderService) {
        this.chemicalFiberCustomerOrderService = chemicalFiberCustomerOrderService;
    }


    @GetMapping
    @Log("查询客户订单")
    @ApiOperation("查询客户订单")
    public ResponseEntity getChemicalFiberCustomerOrders(ChemicalFiberCustomerOrderQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberCustomerOrderService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增客户订单")
    @ApiOperation("新增客户订单")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberCustomerOrder resources){
        return new ResponseEntity<>(chemicalFiberCustomerOrderService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改客户订单")
    @ApiOperation("修改客户订单")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberCustomerOrder resources){
        chemicalFiberCustomerOrderService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除客户订单")
    @ApiOperation("删除客户订单")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberCustomerOrderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/exportCustomerOrder/{id}")
    public void download(HttpServletResponse response,  @PathVariable Integer id) throws IOException {
        chemicalFiberCustomerOrderService.download(id, response);
    }

    @PostMapping(value = "/getProdction")
    @Log("查询客户订单详细信息")
    @ApiOperation("查询客户订单详细信息")
    public ResponseEntity getProdction(@Validated @RequestBody ChemicalFiberCustomerOrder resources){
        return new ResponseEntity<>(chemicalFiberCustomerOrderService.getProdction(resources),HttpStatus.OK);
    }



}
