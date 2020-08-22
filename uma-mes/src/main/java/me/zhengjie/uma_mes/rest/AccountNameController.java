package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.AccountName;
import me.zhengjie.uma_mes.service.AccountNameService;
import me.zhengjie.uma_mes.service.dto.AccountNameQueryCriteria;
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
* @date 2020-08-14
*/
@Api(tags = "AccountName管理")
@RestController
@RequestMapping("/api/accountName")
public class AccountNameController {

    private final AccountNameService accountNameService;

    public AccountNameController(AccountNameService accountNameService) {
        this.accountNameService = accountNameService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('accountName:list')")
    public void download(HttpServletResponse response, AccountNameQueryCriteria criteria) throws IOException {
        accountNameService.download(accountNameService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询AccountName")
    @ApiOperation("查询AccountName")
    @PreAuthorize("@el.check('accountName:list')")
    public ResponseEntity getAccountNames(AccountNameQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(accountNameService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/getAccountList")
    @Log("查询AccountName")
    @ApiOperation("查询AccountName")
    public ResponseEntity getAccountList(AccountNameQueryCriteria criteria){
        return new ResponseEntity<>(accountNameService.queryAll(criteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增AccountName")
    @ApiOperation("新增AccountName")
    @PreAuthorize("@el.check('accountName:add')")
    public ResponseEntity create(@Validated @RequestBody AccountName resources){
        return new ResponseEntity<>(accountNameService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改AccountName")
    @ApiOperation("修改AccountName")
    @PreAuthorize("@el.check('accountName:edit')")
    public ResponseEntity update(@Validated @RequestBody AccountName resources){
        accountNameService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除AccountName")
    @ApiOperation("删除AccountName")
    @PreAuthorize("@el.check('accountName:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        accountNameService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}