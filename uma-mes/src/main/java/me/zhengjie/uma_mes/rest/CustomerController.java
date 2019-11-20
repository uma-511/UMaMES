package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
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
@Api(tags = "优码MES:客户管理")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('customer:list')")
    public void download(HttpServletResponse response, CustomerQueryCriteria criteria) throws IOException {
        customerService.download(customerService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Customer")
    @ApiOperation("查询Customer")
    @PreAuthorize("@el.check('customer:list')")
    public ResponseEntity getCustomers(CustomerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(customerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Customer")
    @ApiOperation("新增Customer")
    @PreAuthorize("@el.check('customer:add')")
    public ResponseEntity create(@Validated @RequestBody Customer resources){
        return new ResponseEntity<>(customerService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Customer")
    @ApiOperation("修改Customer")
    @PreAuthorize("@el.check('customer:edit')")
    public ResponseEntity update(@Validated @RequestBody Customer resources){
        customerService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Customer")
    @ApiOperation("删除Customer")
    @PreAuthorize("@el.check('customer:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        customerService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}