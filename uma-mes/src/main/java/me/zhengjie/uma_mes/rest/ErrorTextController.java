package me.zhengjie.uma_mes.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.service.ErrorTextService;
import me.zhengjie.uma_mes.service.dto.ErrorTextQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "优码MES:手持机错误删除记录")
@RestController
@RequestMapping("/api/errorText")
public class ErrorTextController {

    @Autowired
    private ErrorTextService errorTextService;


    @Log("查询手持机删除记录")
    @ApiOperation("查询手持机删除记录")
    @GetMapping
    //@PreAuthorize("@el.check('customer:list')")
    public ResponseEntity queryAll(ErrorTextQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(errorTextService.queryAll(criteria,pageable), HttpStatus.OK);
    }
}
