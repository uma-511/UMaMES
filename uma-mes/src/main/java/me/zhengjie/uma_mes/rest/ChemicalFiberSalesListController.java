package me.zhengjie.uma_mes.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import me.zhengjie.uma_mes.service.ChemicalFiberSalesListService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberSalesListQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "优码MES:化纤-销售产品报表管理")
@RestController
@RequestMapping("/api/chemicalFiberSalesList")
public class ChemicalFiberSalesListController {


    @Autowired
    private ChemicalFiberSalesListService chemicalFiberSalesListService;

    @GetMapping
    @Log("查询ChemicalFiberSalesList")
    @ApiOperation("查询ChemicalFiberSalesList")
    //@PreAuthorize("@el.check('chemicalFiberDeliveryNote:list')")
    public ResponseEntity queryAll(ChemicalFiberSalesListQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberSalesListService.queryAll(criteria,pageable),HttpStatus.OK);
    }
}
