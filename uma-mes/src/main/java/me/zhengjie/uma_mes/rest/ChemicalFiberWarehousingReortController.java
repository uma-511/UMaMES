package me.zhengjie.uma_mes.rest;

import com.lgmn.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.ChemicalFiberWarehousingReortService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingDetailQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "优码MES:化纤-盘点单列表管理")
@RestController
@RequestMapping("/api/chemicalFiberWarehousingReort")
public class ChemicalFiberWarehousingReortController {

    @Autowired
    private ChemicalFiberWarehousingReortService chemicalFiberWarehousingReortService;


    @GetMapping
    @Log("查询chemicalFiberWarehousingReort")
    @ApiOperation("查询chemicalFiberWarehousingReort")
    //@PreAuthorize("@el.check('chemicalFiberStockLnventoryDetail:list')")
    public ResponseEntity queryAll(ChemicalFiberWarehousingReortQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberWarehousingReortService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    @PostMapping("/getSummaryData")
    @Log("查询ChemicalFiberLabel")
    @ApiOperation("查询ChemicalFiberLabel")
    public Result getSummaryData(@RequestBody ChemicalFiberWarehousingReortQueryCriteria criteria) {

        Map<String, Object> map = chemicalFiberWarehousingReortService.getSummaryData(criteria);
        return Result.success(map);
    }
}
