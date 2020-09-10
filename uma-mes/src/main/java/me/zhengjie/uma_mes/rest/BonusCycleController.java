package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.BonusCycle;
import me.zhengjie.uma_mes.service.BonusCycleService;
import me.zhengjie.uma_mes.service.dto.BonusCycleQueryCriteria;
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
* @date 2020-09-10
*/
@Api(tags = "BonusCycle管理")
@RestController
@RequestMapping("/api/bonusCycle")
public class BonusCycleController {

    private final BonusCycleService bonusCycleService;

    public BonusCycleController(BonusCycleService bonusCycleService) {
        this.bonusCycleService = bonusCycleService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('bonusCycle:list')")
    public void download(HttpServletResponse response, BonusCycleQueryCriteria criteria) throws IOException {
        bonusCycleService.download(bonusCycleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询BonusCycle")
    @ApiOperation("查询BonusCycle")
    @PreAuthorize("@el.check('bonusCycle:list')")
    public ResponseEntity getBonusCycles(BonusCycleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(bonusCycleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增BonusCycle")
    @ApiOperation("新增BonusCycle")
    @PreAuthorize("@el.check('bonusCycle:add')")
    public ResponseEntity create(@Validated @RequestBody BonusCycle resources){
        return new ResponseEntity<>(bonusCycleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改BonusCycle")
    @ApiOperation("修改BonusCycle")
    @PreAuthorize("@el.check('bonusCycle:edit')")
    public ResponseEntity update(@Validated @RequestBody BonusCycle resources){
        bonusCycleService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}