package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.uma_mes.service.ChemicalFiberProductService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
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
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Api(tags = "优码MES:化纤-产品管理")
@RestController
@RequestMapping("/api/chemicalFiberProduct")
public class ChemicalFiberProductController {

    private final ChemicalFiberProductService chemicalFiberProductService;

    public ChemicalFiberProductController(ChemicalFiberProductService chemicalFiberProductService) {
        this.chemicalFiberProductService = chemicalFiberProductService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberProduct:list')")
    public void download(HttpServletResponse response, ChemicalFiberProductQueryCriteria criteria) throws IOException {
        chemicalFiberProductService.download(chemicalFiberProductService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberProduct")
    @ApiOperation("查询ChemicalFiberProduct")
    @PreAuthorize("@el.check('chemicalFiberProduct:list')")
    public ResponseEntity getChemicalFiberProducts(ChemicalFiberProductQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberProductService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberProduct")
    @ApiOperation("新增ChemicalFiberProduct")
    @PreAuthorize("@el.check('chemicalFiberProduct:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberProduct resources){
        return new ResponseEntity<>(chemicalFiberProductService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberProduct")
    @ApiOperation("修改ChemicalFiberProduct")
    @PreAuthorize("@el.check('chemicalFiberProduct:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberProduct resources){
        chemicalFiberProductService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberProduct")
    @ApiOperation("删除ChemicalFiberProduct")
    @PreAuthorize("@el.check('chemicalFiberProduct:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberProductService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}