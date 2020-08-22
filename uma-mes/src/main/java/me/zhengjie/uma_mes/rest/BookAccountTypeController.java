package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.BookAccountType;
import me.zhengjie.uma_mes.service.BookAccountTypeService;
import me.zhengjie.uma_mes.service.dto.BookAccountTypeQueryCriteria;
import org.springframework.cache.annotation.Cacheable;
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
* @date 2020-08-15
*/
@Api(tags = "BookAccountType管理")
@RestController
@RequestMapping("/api/bookAccountType")
public class BookAccountTypeController {

    private final BookAccountTypeService bookAccountTypeService;

    public BookAccountTypeController(BookAccountTypeService bookAccountTypeService) {
        this.bookAccountTypeService = bookAccountTypeService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('bookAccountType:list')")
    public void download(HttpServletResponse response, BookAccountTypeQueryCriteria criteria) throws IOException {
        bookAccountTypeService.download(bookAccountTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询BookAccountType")
    @ApiOperation("查询BookAccountType")
    @PreAuthorize("@el.check('bookAccountType:list')")
    public ResponseEntity getBookAccountTypes(BookAccountTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(bookAccountTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/getBookAccountList")
    @Log("查询BookAccountType")
    @ApiOperation("查询BookAccountType")
    @PreAuthorize("@el.check('bookAccountType:list')")
    public ResponseEntity getBookAccountList(BookAccountTypeQueryCriteria criteria){
        return new ResponseEntity<>(bookAccountTypeService.queryAll(criteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增BookAccountType")
    @ApiOperation("新增BookAccountType")
    @PreAuthorize("@el.check('bookAccountType:add')")
    public ResponseEntity create(@Validated @RequestBody BookAccountType resources){
        return new ResponseEntity<>(bookAccountTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改BookAccountType")
    @ApiOperation("修改BookAccountType")
    @PreAuthorize("@el.check('bookAccountType:edit')")
    public ResponseEntity update(@Validated @RequestBody BookAccountType resources){
        bookAccountTypeService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除BookAccountType")
    @ApiOperation("删除BookAccountType")
    @PreAuthorize("@el.check('bookAccountType:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        bookAccountTypeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}