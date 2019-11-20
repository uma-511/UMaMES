package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaCustomer;
import me.zhengjie.uma_mes.service.UmaCustomerService;
import me.zhengjie.uma_mes.service.dto.UmaCustomerQueryCriteria;
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
@Api(tags = "UmaCustomer管理")
@RestController
@RequestMapping("/api/umaCustomer")
public class UmaCustomerController {

    private final UmaCustomerService umaCustomerService;

    public UmaCustomerController(UmaCustomerService umaCustomerService) {
        this.umaCustomerService = umaCustomerService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaCustomer:list')")
    public void download(HttpServletResponse response, UmaCustomerQueryCriteria criteria) throws IOException {
        umaCustomerService.download(umaCustomerService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaCustomer")
    @ApiOperation("查询UmaCustomer")
    @PreAuthorize("@el.check('umaCustomer:list')")
    public ResponseEntity getUmaCustomers(UmaCustomerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaCustomerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaCustomer")
    @ApiOperation("新增UmaCustomer")
    @PreAuthorize("@el.check('umaCustomer:add')")
    public ResponseEntity create(@Validated @RequestBody UmaCustomer resources){
        return new ResponseEntity<>(umaCustomerService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaCustomer")
    @ApiOperation("修改UmaCustomer")
    @PreAuthorize("@el.check('umaCustomer:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaCustomer resources){
        umaCustomerService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaCustomer")
    @ApiOperation("删除UmaCustomer")
    @PreAuthorize("@el.check('umaCustomer:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaCustomerService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}