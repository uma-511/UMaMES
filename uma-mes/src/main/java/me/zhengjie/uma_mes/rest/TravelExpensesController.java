package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.TravelExpenses;
import me.zhengjie.uma_mes.service.TravelExpensesService;
import me.zhengjie.uma_mes.service.dto.TravelExpensesQueryCriteria;
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
* @author wave
* @date 2020-09-02
*/
@Api(tags = "TravelExpenses管理")
@RestController
@RequestMapping("/api/travelExpenses")
public class TravelExpensesController {

    private final TravelExpensesService travelExpensesService;

    public TravelExpensesController(TravelExpensesService travelExpensesService) {
        this.travelExpensesService = travelExpensesService;
    }

    @GetMapping
    @Log("查询TravelExpenses")
    @ApiOperation("查询TravelExpenses")
    //@PreAuthorize("@el.check('travelExpenses:list')")
    public ResponseEntity getTravelExpensess(TravelExpensesQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(travelExpensesService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增TravelExpenses")
    @ApiOperation("新增TravelExpenses")
    // @PreAuthorize("@el.check('travelExpenses:add')")
    public ResponseEntity create(@Validated @RequestBody TravelExpenses resources){
        return new ResponseEntity<>(travelExpensesService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改TravelExpenses")
    @ApiOperation("修改TravelExpenses")
    //@PreAuthorize("@el.check('travelExpenses:edit')")
    public ResponseEntity update(@Validated @RequestBody TravelExpenses resources){
        travelExpensesService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除TravelExpenses")
    @ApiOperation("删除TravelExpenses")
    //@PreAuthorize("@el.check('travelExpenses:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        travelExpensesService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}