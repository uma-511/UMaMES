package me.zhengjie.uma_mes.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberProductMenu;
import me.zhengjie.uma_mes.service.ChemicalFiberProductMenuService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "优码MES:化纤-产品管理目录管理")
@RestController
@RequestMapping("/api/chemicalFiberProductMenus")
public class ChemicalFiberProductMenuController {


    private final ChemicalFiberProductMenuService chemicalFiberProductMenuService;

    public ChemicalFiberProductMenuController(ChemicalFiberProductMenuService chemicalFiberProductMenuService){
        this.chemicalFiberProductMenuService = chemicalFiberProductMenuService;
    }

    @GetMapping
    @Log("查询ChemicalFiberProductMenu")
    @ApiOperation("查询ChemicalFiberProductMenu")
    //@PreAuthorize("@el.check('chemicalFiberProductMenu:list')")
    public ResponseEntity getChemicalFiberProductMenus(ChemicalFiberProductMenuQueryCriteria criteria){
        List<ChemicalFiberProductMenuDTO> productMenu = chemicalFiberProductMenuService.queryAll(criteria);
        return new ResponseEntity<>(productMenu, HttpStatus.OK);
    }

    @GetMapping(value = "/add")
    @Log("新增ChemicalFiberProductMenu")
    @ApiOperation("新增ChemicalFiberProductMenu")
    //@PreAuthorize("@el.check('chemicalFiberProductMenu:add')")
    public ResponseEntity addChemicalFiberProductMenus(ChemicalFiberProductMenu resources){
        return new ResponseEntity<>(chemicalFiberProductMenuService.create(resources), HttpStatus.CREATED);
    }


}
